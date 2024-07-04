package cn.bootx.platform.iam.service.permission;

import cn.bootx.platform.core.anno.RequestGroup;
import cn.bootx.platform.iam.dao.permission.PermPathManager;
import cn.bootx.platform.iam.dto.permission.RequestPath;
import cn.bootx.platform.iam.entity.permission.PermPath;
import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 访问路径生成服务
 * @author xxm
 * @since 2024/7/4
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PermPathSyncService {

    private final PermPathManager permPathManager;

    private final WebApplicationContext applicationContext;

    private final static String REQUEST_MAPPING_HANDLER_MAPPING = "requestMappingHandlerMapping";

    /**
     * 同步
     */
    public void sync() {
        // 获取系统中的请求路径
        List<RequestPath> requestPaths = this.getRequestPath();

        // 查询数据中的数据并转换为请求信息列表
        List<PermPath> permPaths = permPathManager.findAll();

        Map<String, RequestPath> requestPathMap = requestPaths.stream()
                .collect(Collectors.toMap(o -> o.getPath() + ":" + o.getRequestType(), Function.identity()));
        Map<String, PermPath> permPathMap = permPaths.stream()
                .collect(Collectors.toMap(o -> o.getPath() + ":" + o.getRequestType(), Function.identity()));

        // 需要信息的数据
        List<PermPath> addData = this.getAddData(requestPathMap, permPathMap);
        // 需要删除的数据
        List<Long> deleteIds = this.getDeleteData(requestPathMap, permPathMap);
        // 需要更新的数据
        List<PermPath> updateData = this.getUpdateData(requestPathMap, permPathMap);

        // 保存新增的
        permPathManager.saveAll(addData);
        // 更新存在的
        permPathManager.updateAllById(updateData);
        // 删除不再需要的
        permPathManager.deleteByIds(deleteIds);

        // 重建树结构 删除非子节点
        permPathManager.deleteNotChild();
        // 生成模块信息
        Map<String, PermPath> moduleMap = this.builderModule(requestPaths);
        // 生成分组信息
        Map<String, PermPath> groupMap = this.builderGroup(requestPaths);
        // 合并进行保存
        ArrayList<PermPath> list = new ArrayList<>();
        list.addAll(moduleMap.values());
        list.addAll(groupMap.values());
        permPathManager.saveAll(list);

    }

    /**
     * 获取新增的
     */
    public List<PermPath> getAddData(Map<String, RequestPath> requestPathMap, Map<String, PermPath> permPathMap){
        return requestPathMap.keySet().stream()
                .filter(o -> !permPathMap.containsKey(o))
                .map(requestPathMap::get)
                .map(o -> new PermPath()
                        .setParentCode(o.getGroupCode())
                        .setRequestType(o.getRequestType())
                        .setPath(o.getPath())
                        .setName(o.getName())
                        .setLeaf(true)
                )
                .toList();
    }

    /**
     * 获取删除的
     */
    public List<Long> getDeleteData(Map<String, RequestPath> requestPathMap, Map<String, PermPath> permPathMap){
        return permPathMap.keySet().stream()
                .filter(o -> !requestPathMap.containsKey(o))
                .map(permPathMap::get)
                .map(PermPath::getId)
                .toList();

    }

    /**
     * 获取要更新的
     */
    public List<PermPath> getUpdateData(Map<String, RequestPath> requestPathMap, Map<String, PermPath> permPathMap){
        return permPathMap.keySet().stream()
                .filter(requestPathMap::containsKey)
                .map(permPathMap::get)
                .peek(o -> {
                    RequestPath requestPath = requestPathMap.get(o.getPath() + ":" + o.getRequestType());
                    o.setName(requestPath.getName())
                            .setParentCode(requestPath.getGroupCode());
                }).toList();
    }


    /**
     * 生成模块对应的实体
     */
    private Map<String,PermPath> builderModule(List<RequestPath> allPathList) {
        // 提取模块名称和编码, 模块有多个名字情况下获取其中的一个
        Map<String, String> moduleCodeNameMap = allPathList.stream()
                .collect(Collectors.toMap(RequestPath::getModuleCode, RequestPath::getModuleName, (v1, v2) -> v1));
        // 模块名称和编码生成模块信息并返回分组
        return moduleCodeNameMap.keySet()
                .stream()
                .map(o -> new PermPath().setCode(o)
                        .setName(moduleCodeNameMap.get(o)))
                .collect(Collectors.toMap(PermPath::getCode, Function.identity()));
    }

    /**
     * 生成组对应的实体
     */
    private Map<String, PermPath> builderGroup(List<RequestPath> allPathList) {
        // 提取组名称和编码, 组有多个名字情况下获取其中的一个
        Map<String, List<RequestPath>> groupMap = allPathList.stream()
                .collect(Collectors.groupingBy(RequestPath::getGroupCode));
        // 组名称和编码生成组信息并返回分组
        // 生成组信息
        List<PermPath> groupList = new ArrayList<>(groupMap.size());
        for (String groupCode : groupMap.keySet()) {
            // 组 code name
            String groupName = groupMap.get(groupCode)
                    .stream()
                    .map(RequestPath::getGroupName)
                    .findFirst()
                    .orElse(null);
            // 模块code
            String moduleCode = groupMap.get(groupCode)
                    .stream()
                    .map(RequestPath::getModuleCode)
                    .findFirst()
                    .orElse(null);
            PermPath permPath = new PermPath().setCode(groupCode)
                    .setName(groupName)
                    .setParentCode(moduleCode);
            groupList.add(permPath);
        }
        return groupList.stream()
                .collect(Collectors.toMap(PermPath::getCode, Function.identity()));
    }

    /**
     * 获取系统请求列表
     */
    public List<RequestPath> getRequestPath(){
        RequestMappingHandlerMapping mapping = applicationContext.getBean(REQUEST_MAPPING_HANDLER_MAPPING,
                RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

        // 进行过滤, 只保留带有路径权限标识的映射
        List<RequestMappingInfo> mappingInfos = map.keySet()
                .stream()
                .filter(pathKey -> {
                    HandlerMethod handlerMethod = map.get(pathKey);
                    return Objects.nonNull(handlerMethod.getMethodAnnotation(cn.bootx.platform.core.anno.RequestPath.class))
                            &&Objects.nonNull(handlerMethod.getBeanType().getAnnotation(RequestGroup.class));
                }).toList();

        // 获取url与类和方法的对应信息
        return mappingInfos.stream()
                .map(requestMappingInfo -> {
                    // 根据请求方式和路径构建请求权限对象
                    HandlerMethod handlerMethod = map.get(requestMappingInfo);
                    return this.builderRequestPath(requestMappingInfo, handlerMethod);
                })
                .flatMap(Collection::stream)
                .toList();
    }

    /**
     * 根据系统重映射配置生成请求路径对象
     */
    private List<RequestPath> builderRequestPath(RequestMappingInfo requestMappingInfo, HandlerMethod handlerMethod) {
        Method method = handlerMethod.getMethod();
        Class<?> beanClass = method.getDeclaringClass();
        // 请求路径
        List<String> paths = Optional.ofNullable(requestMappingInfo.getPathPatternsCondition())
                .map(PathPatternsRequestCondition::getPatternValues)
                .map(CollUtil::newArrayList)
                .orElse(new ArrayList<>(1));
        if (CollUtil.isEmpty(paths)) {
            return new ArrayList<>(0);
        }

        // 处理请求类型和请求路径
        List<String> requestMethods = requestMappingInfo.getMethodsCondition()
                .getMethods()
                .stream()
                .map(Enum::name)
                .collect(Collectors.toList());
        List<RequestPath> list = paths.stream()
                .map(path -> this.builderRequestPath(path, requestMethods))
                .flatMap(Collection::stream)
                .toList();

        // 读取控制器注解和请求方法注解
        RequestGroup requestGroup = beanClass.getAnnotation(RequestGroup.class);
        cn.bootx.platform.core.anno.RequestPath requestPath = method.getAnnotation(cn.bootx.platform.core.anno.RequestPath.class);

        // 设置通用属性
        for (RequestPath path : list) {
            path.setModuleCode(requestGroup.moduleCode())
                    .setModuleName(requestGroup.moduleName())
                    .setGroupCode(requestGroup.groupCode())
                    .setGroupName(requestGroup.groupName())
                    .setName(requestPath.value());
        }
        return list;
    }

    /**
     * 构建请求路径路径和请求方式
     */
    private List<RequestPath> builderRequestPath(String path, List<String> requestMethods) {
        return requestMethods.stream()
                .map(requestMethod -> new RequestPath()
                        .setPath(path)
                        .setRequestType(requestMethod))
                .collect(Collectors.toList());
    }

}
