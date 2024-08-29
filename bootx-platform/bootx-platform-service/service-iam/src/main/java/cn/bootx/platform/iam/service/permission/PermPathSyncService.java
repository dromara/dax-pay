package cn.bootx.platform.iam.service.permission;

import cn.bootx.platform.common.config.BootxConfigProperties;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.iam.dao.permission.PermPathManager;
import cn.bootx.platform.iam.dao.upms.RolePathManager;
import cn.bootx.platform.iam.bo.permission.RequestPathBo;
import cn.bootx.platform.iam.entity.permission.PermPath;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
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

import static java.util.stream.Collectors.toList;

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

    private final RolePathManager rolePathManager;

    private final WebApplicationContext applicationContext;

    private final BootxConfigProperties bootxConfigProperties;

    private final static String REQUEST_MAPPING_HANDLER_MAPPING = "requestMappingHandlerMapping";

    /**
     * 同步
     */
    @CacheEvict(value = "cache:permPath", allEntries = true)
    public void sync() {
        String clientCode = bootxConfigProperties.getClientCode();
        // 获取系统中的请求路径
        List<RequestPathBo> requestPathBos = this.getRequestPath();

        // 查询数据中的数据并转换为请求信息列表
        List<PermPath> permPaths = permPathManager.findAllByLeafAndClient(true,clientCode);
        var requestPathMap = requestPathBos.stream()
                .collect(Collectors.toMap(o -> o.getPath() + ":" + o.getMethod(), Function.identity()));
        var permPathMap = permPaths.stream()
                .collect(Collectors.toMap(o -> o.getPath() + ":" + o.getMethod(), Function.identity()));

        // 需要信息的数据
        List<PermPath> addData = this.getAddData(requestPathMap, permPathMap);
        // 需要删除的数据
        List<Long> deleteIds = this.getDeleteData(requestPathMap, permPathMap);
        // 需要更新的数据
        List<PermPath> updateData = this.getUpdateData(requestPathMap, permPathMap);

        // 保存新增的
        addData.forEach(o -> o.setClientCode(clientCode));
        permPathManager.saveAll(addData);
        // 更新存在的
        permPathManager.updateAllById(updateData);
        // 删除不再需要的
        permPathManager.deleteByIds(deleteIds);
        // 删除关联关系
        rolePathManager.deleteByPathIds(deleteIds);

        // 重建树结构 删除指定终端的非子节点
        permPathManager.deleteNotChild(clientCode);
        // 生成模块信息
        var moduleMap = this.builderModule(requestPathBos);
        // 生成分组信息
        var groupMap = this.builderGroup(requestPathBos);
        // 合并进行保存
        ArrayList<PermPath> list = new ArrayList<>();
        list.addAll(moduleMap);
        list.addAll(groupMap);
        // 设置终端编码
        list.forEach(o -> o.setClientCode(clientCode));
        permPathManager.saveAll(list);

    }

    /**
     * 获取新增的
     */
    public List<PermPath> getAddData(Map<String, RequestPathBo> requestPathMap, Map<String, PermPath> permPathMap){
        return requestPathMap.keySet().stream()
                .filter(o -> !permPathMap.containsKey(o))
                .map(requestPathMap::get)
                .map(o -> new PermPath()
                        .setParentCode(o.getGroupCode())
                        .setMethod(o.getMethod())
                        .setPath(o.getPath())
                        .setName(o.getName())
                        .setLeaf(true)
                )
                .toList();
    }

    /**
     * 获取删除的
     */
    public List<Long> getDeleteData(Map<String, RequestPathBo> requestPathMap, Map<String, PermPath> permPathMap){
        return permPathMap.keySet().stream()
                .filter(o -> !requestPathMap.containsKey(o))
                .map(permPathMap::get)
                .map(PermPath::getId)
                .toList();

    }

    /**
     * 获取要更新的
     */
    public List<PermPath> getUpdateData(Map<String, RequestPathBo> requestPathMap, Map<String, PermPath> permPathMap){
        return permPathMap.keySet().stream()
                .filter(requestPathMap::containsKey)
                .map(permPathMap::get)
                .peek(o -> {
                    RequestPathBo requestPathBo = requestPathMap.get(o.getPath() + ":" + o.getMethod());
                    o.setName(requestPathBo.getName())
                            .setParentCode(requestPathBo.getGroupCode());
                }).toList();
    }


    /**
     * 生成模块对应的实体
     */
    private List<PermPath> builderModule(List<RequestPathBo> allPathList) {
        // 提取模块名称和编码, 模块有多个名字情况下获取其中的一个
        Map<String, String> moduleCodeNameMap = allPathList.stream()
                .filter(o-> StrUtil.isNotBlank(o.getModuleName()))
                .collect(HashMap::new,(map,item)->map.put(item.getModuleCode(),item.getModuleName()), HashMap::putAll);
        // 模块名称和编码生成模块信息并返回分组
        return moduleCodeNameMap.keySet()
                .stream()
                .map(o -> new PermPath().setCode(o)
                        .setName(moduleCodeNameMap.get(o)))
                .toList();
    }

    /**
     * 生成组对应的实体
     */
    private List<PermPath> builderGroup(List<RequestPathBo> allPathList) {
        // 提取组名称和编码, 组有多个名字情况下获取其中的一个
        Map<String, List<RequestPathBo>> groupMap = allPathList.stream()
                .collect(Collectors.groupingBy(RequestPathBo::getGroupCode));
        // 组名称和编码生成组信息并返回分组
        // 生成组信息
        List<PermPath> groupList = new ArrayList<>(groupMap.size());
        for (String groupCode : groupMap.keySet()) {
            // 组 code name
            String groupName = groupMap.get(groupCode)
                    .stream()
                    .map(RequestPathBo::getGroupName)
                    .filter(StrUtil::isNotBlank)
                    .findFirst()
                    .orElse(null);
            // 模块code
            String moduleCode = groupMap.get(groupCode)
                    .stream()
                    .map(RequestPathBo::getModuleCode)
                    .findFirst()
                    .orElse(null);
            PermPath permPath = new PermPath().setCode(groupCode)
                    .setName(groupName)
                    .setParentCode(moduleCode);
            groupList.add(permPath);
        }
        return groupList;
    }

    /**
     * 获取系统请求列表
     */
    public List<RequestPathBo> getRequestPath(){
        RequestMappingHandlerMapping mapping = applicationContext.getBean(REQUEST_MAPPING_HANDLER_MAPPING, RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

        // 进行过滤, 只保留带有路径权限标识的映射
        List<RequestMappingInfo> mappingInfos = map.keySet()
                .stream()
                .filter(pathKey -> {
                    HandlerMethod handlerMethod = map.get(pathKey);
                    return Objects.nonNull(handlerMethod.getMethodAnnotation(cn.bootx.platform.core.annotation.RequestPath.class))
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
    private List<RequestPathBo> builderRequestPath(RequestMappingInfo requestMappingInfo, HandlerMethod handlerMethod) {
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
                .collect(toList());
        List<RequestPathBo> list = paths.stream()
                .map(path -> this.builderRequestPath(path, requestMethods))
                .flatMap(Collection::stream)
                .toList();

        // 读取控制器注解和请求方法注解
        RequestGroup requestGroup = beanClass.getAnnotation(RequestGroup.class);
        cn.bootx.platform.core.annotation.RequestPath requestPath = method.getAnnotation(cn.bootx.platform.core.annotation.RequestPath.class);

        // 设置通用属性
        for (RequestPathBo path : list) {
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
    private List<RequestPathBo> builderRequestPath(String path, List<String> requestMethods) {
        return requestMethods.stream()
                .map(requestMethod -> new RequestPathBo()
                        .setPath(path)
                        .setMethod(requestMethod))
                .collect(toList());
    }

}
