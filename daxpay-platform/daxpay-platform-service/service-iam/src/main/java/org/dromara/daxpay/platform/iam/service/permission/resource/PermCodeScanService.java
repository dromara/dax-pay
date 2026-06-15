package org.dromara.daxpay.platform.iam.service.permission.resource;

import org.dromara.daxpay.platform.core.annotation.PermCode;
import org.dromara.daxpay.platform.core.exception.ValidationFailedException;
import org.dromara.daxpay.platform.core.util.PermCodeUtil;
import org.dromara.daxpay.platform.iam.dao.permission.PermCodeManager;
import org.dromara.daxpay.platform.iam.dao.permission.PermMenuManager;
import org.dromara.daxpay.platform.iam.dao.upms.RoleCodeManager;
import org.dromara.daxpay.platform.iam.entity.permission.PermCodeData;
import org.dromara.daxpay.platform.iam.param.permission.resource.PermCodeScanParam;
import org.dromara.daxpay.platform.iam.result.permission.resource.PermCodeScanResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/// # 权限码扫描服务
///
/// 扫描固定业务包 `org.dromara.daxpay` 下已注册的 Spring MVC 请求处理器，收集
/// {@link PermCode} 注解声明的权限码定义，并与权限码主数据表进行同步。
/// 同步策略如下：
/// 1. 代码中存在、数据库中不存在：新增为内置权限码。
/// 2. 代码中存在、数据库中也存在，但名称或菜单编码变化：更新主数据。
/// 3. 代码中与数据库中完全一致：跳过。
/// 4. 数据库中存在、代码中已不存在：删除权限码，并联动删除角色权限关系。
/// 该服务用于保证 `@PermCode` 声明与权限码主数据长期一致，避免人工维护偏差。
@Slf4j
@Service
@RequiredArgsConstructor
public class PermCodeScanService {

    /// 固定业务扫描包，仅扫描当前项目业务代码中的权限声明。
    private static final String BASE_PACKAGE = "org.dromara.daxpay";

    /// Spring 应用上下文，用于获取已注册的请求映射信息。
    private final ApplicationContext applicationContext;
    /// 权限码主数据管理器。
    private final PermCodeManager permCodeManager;
    /// 菜单主数据管理器，用于校验 menuCode 是否存在。
    private final PermMenuManager permMenuManager;
    /// 角色与权限码关系管理器，用于删除失效权限码对应的关系数据。
    private final RoleCodeManager roleCodeManager;

    /// 扫描并同步权限码主数据。
    /// 扫描结果会根据新增、更新、跳过、删除分别计入返回对象，便于前端直接展示同步摘要。
    @Transactional(rollbackFor = Exception.class)
    public PermCodeScanResult scan(PermCodeScanParam param) {
        Map<String, PermCodeDefinition> definitionMap = this.collectDefinitions();
        this.validateDefinitions(definitionMap);

        PermCodeScanResult result = new PermCodeScanResult();
        Map<String, PermCodeData> existedMap = permCodeManager.findAll().stream()
                .collect(Collectors.toMap(PermCodeData::getCode, item -> item, (a, b) -> a));

        List<PermCodeData> addList = new ArrayList<>();
        List<PermCodeData> updateList = new ArrayList<>();
        List<Long> deleteIds = new ArrayList<>();

        definitionMap.values().stream()
                .sorted(Comparator.comparing(PermCodeDefinition::getCode))
                .forEach(definition -> {
                    PermCodeData existed = existedMap.remove(definition.getCode());
                    if (Objects.isNull(existed)) {
                        PermCodeData entity = new PermCodeData()
                                .setCode(definition.getCode())
                                .setNameCn(definition.getNameCn())
                                .setNameEn(definition.getNameEn())
                                .setMenuCode(definition.getMenuCode())
                                .setInternal(true)
                                .setRemark("由 @PermCode 扫描同步生成");
                        addList.add(entity);
                        result.getAddedCodes().add(definition.getCode());
                        result.setAddedCount(result.getAddedCount() + 1);
                        return;
                    }
                    boolean changed = !Objects.equals(existed.getNameCn(), definition.getNameCn())
                            || !Objects.equals(existed.getNameEn(), definition.getNameEn())
                            || !Objects.equals(existed.getMenuCode(), definition.getMenuCode())
                            || !existed.isInternal();
                    if (changed) {
                        existed.setNameCn(definition.getNameCn());
                        existed.setNameEn(definition.getNameEn());
                        existed.setMenuCode(definition.getMenuCode());
                        existed.setInternal(true);
                        if (StrUtil.isBlank(existed.getRemark())) {
                            existed.setRemark("由 @PermCode 扫描同步生成");
                        }
                        updateList.add(existed);
                        result.getUpdatedCodes().add(definition.getCode());
                        result.setUpdatedCount(result.getUpdatedCount() + 1);
                    } else {
                        result.getSkippedCodes().add(definition.getCode());
                        result.setSkippedCount(result.getSkippedCount() + 1);
                    }
                });

        existedMap.values().stream()
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(PermCodeData::getCode))
                .forEach(item -> {
                    deleteIds.add(item.getId());
                    result.getDeletedCodes().add(item.getCode());
                    result.setDeletedCount(result.getDeletedCount() + 1);
                });

        permCodeManager.saveAll(addList);
        permCodeManager.updateAllById(updateList);
        roleCodeManager.deleteByCodeIds(deleteIds);
        permCodeManager.deleteByIds(deleteIds);

        log.info("权限码扫描同步完成, 新增:{}, 更新:{}, 跳过:{}, 删除:{}, 异常:{}",
                result.getAddedCount(), result.getUpdatedCount(), result.getSkippedCount(), result.getDeletedCount(), result.getErrorCount());
        if (CollUtil.isNotEmpty(result.getAddedCodes())) {
            log.info("权限码扫描新增: {}", result.getAddedCodes());
        }
        if (CollUtil.isNotEmpty(result.getUpdatedCodes())) {
            log.info("权限码扫描更新: {}", result.getUpdatedCodes());
        }
        if (CollUtil.isNotEmpty(result.getDeletedCodes())) {
            log.info("权限码扫描删除: {}", result.getDeletedCodes());
        }
        return result;
    }

    /// 收集所有权限码定义。
    /// 这里会遍历 Spring MVC 主请求映射中已注册的处理器方法，只处理固定业务包下同时声明了
    /// {@link PermCode} 的控制器或方法，并对同编码的重复定义进行冲突检测。
    /// 这里显式获取 Bean 名为 `requestMappingHandlerMapping` 的 MVC 主映射，
    /// 不直接按类型获取 {@link RequestMappingHandlerMapping}，避免在启用 Actuator 等场景下
    /// 同时命中 `controllerEndpointHandlerMapping` 等其它映射 Bean，导致注入歧义。
    private Map<String, PermCodeDefinition> collectDefinitions() {
        Map<String, PermCodeDefinition> definitionMap = new HashMap<>();
        Set<String> conflicts = new HashSet<>();
        // 仅扫描 Spring MVC 主请求映射，避免将 Actuator Controller Endpoint 等其它映射纳入权限码扫描范围。
        RequestMappingHandlerMapping handlerMapping = applicationContext.getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);

        for (var entry : handlerMapping.getHandlerMethods().entrySet()) {
            HandlerMethod handlerMethod = entry.getValue();
            Class<?> beanType = handlerMethod.getBeanType();
            if (!beanType.getPackageName().startsWith(BASE_PACKAGE)) {
                continue;
            }
            PermCode classPermCode = beanType.getAnnotation(PermCode.class);
            PermCode methodPermCode = handlerMethod.getMethodAnnotation(PermCode.class);
            if (Objects.isNull(classPermCode) && Objects.isNull(methodPermCode)) {
                continue;
            }

            String code = PermCodeUtil.resolveFullCode(classPermCode, methodPermCode);
            if (StrUtil.isBlank(code)) {
                continue;
            }
            String menuCode = PermCodeUtil.resolveMenuCode(classPermCode, methodPermCode);
            String nameCn = PermCodeUtil.resolveNameCn(classPermCode, methodPermCode);
            String nameEn = PermCodeUtil.resolveNameEn(classPermCode, methodPermCode);
            Method method = handlerMethod.getMethod();
            String location = beanType.getName() + "#" + method.getName();

            PermCodeDefinition definition = new PermCodeDefinition()
                    .setCode(code)
                    .setNameCn(nameCn)
                    .setNameEn(nameEn)
                    .setMenuCode(menuCode)
                    .setLocation(location);
            PermCodeDefinition existed = definitionMap.get(code);
            if (Objects.isNull(existed)) {
                definitionMap.put(code, definition);
                continue;
            }
            if (!Objects.equals(existed.getNameCn(), definition.getNameCn())
                    || !Objects.equals(existed.getNameEn(), definition.getNameEn())
                    || !Objects.equals(existed.getMenuCode(), definition.getMenuCode())) {
                conflicts.add(code + "(" + existed.getLocation() + " / " + definition.getLocation() + ")");
            }
        }
        if (CollUtil.isNotEmpty(conflicts)) {
            // 权限: 权限码定义冲突
            throw new ValidationFailedException("error.iam.perm.codeDefinitionConflict", String.join(", ", conflicts));
        }
        return definitionMap;
    }

    /// 校验扫描出的权限码定义是否合法。
    /// 主要校验权限码编码、中英文名称是否完整，不限制菜单与权限码的维护先后顺序。
    private void validateDefinitions(Map<String, PermCodeDefinition> definitionMap) {
        if (definitionMap.isEmpty()) {
            return;
        }
        if (definitionMap.values().stream().map(PermCodeDefinition::getCode).anyMatch(StrUtil::isBlank)) {
            // 权限: 存在未配置 code 的权限码声明
            throw new ValidationFailedException("error.iam.perm.codeNotConfigured");
        }
        if (definitionMap.values().stream().map(PermCodeDefinition::getNameCn).anyMatch(StrUtil::isBlank)) {
            // 权限: 存在未配置 nameCn 的权限码声明
            throw new ValidationFailedException("error.iam.perm.nameCnNotConfigured");
        }
        if (definitionMap.values().stream().map(PermCodeDefinition::getNameEn).anyMatch(StrUtil::isBlank)) {
            // 权限: 存在未配置 nameEn 的权限码声明
            throw new ValidationFailedException("error.iam.perm.nameEnNotConfigured");
        }
    }

    /// # 扫描得到的权限码定义。
    ///
    /// 用于在同步前临时承载代码中的权限码元数据以及其声明位置。
    @Data
    @Accessors(chain = true)
    private static class PermCodeDefinition {
        /// 权限码编码。
        private String code;
        /// 中文名称。
        private String nameCn;
        /// 英文名称。
        private String nameEn;
        /// 归属菜单编码。
        private String menuCode;
        /// 注解声明位置，格式为 全限定类名#方法名。
        private String location;
    }
}


