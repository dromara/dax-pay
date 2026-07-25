package cn.daxpay.open.platform.common.config;

import org.springframework.boot.EnvironmentPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/// # 生产部署模式启动期校验器
///
/// 通过 SPI(`META-INF/spring.factories`, 键 `org.springframework.boot.EnvironmentPostProcessor`) 注册,
/// 在 [ConfigurableEnvironment] 就绪、ApplicationContext 创建之前运行, 是 Spring Boot 官方的
/// fail-fast 扩展点(与端口冲突/数据源未配等启动失败的机制同源)。
///
/// 工作流程:
/// - 推断部署模式: 显式 `daxpay.platform.deployment.mode` > 按 `spring.profiles.active` 推断(含 prod → PROD, 否则 → DEV)
/// - `PROD` 模式下校验一组"开发态功能"开关, 收集所有违规后一次性抛 [IllegalStateException] 拒绝启动
/// - `DEV` 模式下完全跳过校验, 保持现有开发体验
///
/// 设计原则: **只校验、不覆盖**。运维必须自行在 `application-prod.yml` / 环境变量中把开发态功能关闭,
/// 框架不偷偷改写配置, 全程透明。
///
/// @see cn.daxpay.open.platform.common.config.properties.DeploymentProperties
public class DeploymentModeEnforcer implements EnvironmentPostProcessor {

    /// actuator 敏感端点黑名单(暴露这些会泄露环境变量/线程栈/内部结构/可关停应用)
    ///
    /// 不含 `metrics`/`info`/`health`/`prometheus`(由项目 `application-prod.yml` 显式放行)
    private static final Set<String> SENSITIVE_ACTUATOR_ENDPOINTS = Set.of(
            "env", "heapdump", "threaddump", "beans", "loggers",
            "mappings", "configprops", "caches", "conditions",
            "scheduledtasks", "startup", "httpexchanges",
            "auditevents", "shutdown", "prometheus"
    );

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment env, SpringApplication app) {
        // 推断部署模式
        String mode = resolveMode(env);
        if (!"PROD".equals(mode)) {
            // DEV 模式不校验, 保持现状
            return;
        }

        // ERROR 级校验: 收集所有违规后一次性 fail fast(避免"修一个重启再发现一个"的折磨)
        List<String> errors = new ArrayList<>();
        // boolean 参数第二位 = 该属性未显式配置时的"危险默认值"(框架/Java 默认是否为 true)
        checkBooleanFalse(env, "daxpay.platform.config.sandbox-enabled", true, "沙箱环境全局开关", errors);
        checkBooleanFalse(env, "daxpay.platform.starter.auth.enable-admin", true, "超级管理员登录", errors);
        checkBooleanFalse(env, "daxpay.platform.common.exception.show-full-message", false, "异常详情展示", errors);
        checkBooleanFalse(env, "springdoc.api-docs.enabled", true, "OpenAPI 文档", errors);
        checkBooleanFalse(env, "springdoc.swagger-ui.enabled", true, "Swagger UI", errors);
        checkBooleanFalse(env, "daxpay.platform.common.spring.cors.enable", false, "应用层 CORS", errors);
        checkActuatorEndpoints(env, errors);
        checkHealthShowDetails(env, errors);
        checkIgnoreUrls(env, errors);

        // 有 ERROR 则拒绝启动(失败信息由 Spring Boot 启动失败机制打印, 通过则静默)
        if (!errors.isEmpty()) {
            throw new IllegalStateException(buildFailureMessage(errors));
        }
    }

    /// 推断部署模式: 显式配置优先, 否则按 active profile 推断
    private String resolveMode(ConfigurableEnvironment env) {
        String explicit = env.getProperty("daxpay.platform.deployment.mode");
        if (explicit != null && !explicit.isBlank()) {
            return explicit.toUpperCase();
        }
        // 未显式配置: 含 prod profile → PROD, 否则 → DEV
        for (String profile : env.getActiveProfiles()) {
            if ("prod".equalsIgnoreCase(profile)) {
                return "PROD";
            }
        }
        return "DEV";
    }

    /// 校验 boolean 属性必须为 false
    ///
    /// @param key              配置键
    /// @param dangerousDefault 该属性未显式配置时的框架/Java 默认值是否为 true(危险)
    /// @param label            人读说明
    private void checkBooleanFalse(ConfigurableEnvironment env, String key, boolean dangerousDefault,
                                   String label, List<String> errors) {
        Boolean value = env.getProperty(key, Boolean.class);
        // 未显式配置时按 dangerousDefault 推断实际生效值(ConfigurationProperties 绑定阶段会用 Java 默认值)
        boolean effective = (value == null) ? dangerousDefault : value;
        if (effective) {
            String actual = (value == null) ? "(未配置, 框架默认 true)" : "true";
            errors.add("  - " + key + " = " + actual + "  [" + label + " 生产环境必须为 false]");
        }
    }

    /// 校验 actuator 端点暴露不含敏感端点
    private void checkActuatorEndpoints(ConfigurableEnvironment env, List<String> errors) {
        String include = env.getProperty("management.endpoints.web.exposure.include", "");
        Set<String> exposed = Arrays.stream(include.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
        if (exposed.isEmpty()) {
            // 未配置时 Spring Boot 默认仅暴露 health/info, 安全
            return;
        }
        if (exposed.contains("*")) {
            errors.add("  - management.endpoints.web.exposure.include = *  [禁止全量暴露 actuator 端点, 仅允许 health,info,metrics]");
            return;
        }
        List<String> hit = exposed.stream()
                .filter(SENSITIVE_ACTUATOR_ENDPOINTS::contains)
                .sorted()
                .toList();
        if (!hit.isEmpty()) {
            errors.add("  - management.endpoints.web.exposure.include 含敏感端点: " + hit
                    + "  [生产仅允许 health,info,metrics]");
        }
    }

    /// 校验 health 端点不对外暴露详情(always 会泄露 DB/Redis 内部状态)
    private void checkHealthShowDetails(ConfigurableEnvironment env, List<String> errors) {
        String showDetails = env.getProperty("management.endpoint.health.show-details", "never");
        if ("always".equalsIgnoreCase(showDetails)) {
            errors.add("  - management.endpoint.health.show-details = always  [会暴露 DB/Redis 内部状态, 改为 never 或 when-authorized]");
        }
    }

    /// 校验认证放行路径不含通配全开(等于完全关闭认证)
    private void checkIgnoreUrls(ConfigurableEnvironment env, List<String> errors) {
        List<String> urls = Binder.get(env)
                .bind("daxpay.platform.starter.auth.ignore-urls", Bindable.listOf(String.class))
                .orElse(Collections.emptyList());
        if (urls.stream().anyMatch("/**"::equals)) {
            errors.add("  - daxpay.platform.starter.auth.ignore-urls 含 '/**'  [会完全关闭认证, 仅 dev 允许]");
        }
    }

    private String buildFailureMessage(List<String> errors) {
        return System.lineSeparator()
                + "----------------------------------------------------------" + System.lineSeparator()
                + "  生产部署模式(PROD)启动校验失败" + System.lineSeparator()
                + "  检测到 " + errors.size() + " 项开发态功能未关闭:" + System.lineSeparator()
                + String.join(System.lineSeparator(), errors) + System.lineSeparator()
                + "----------------------------------------------------------" + System.lineSeparator()
                + "  请在 application-prod.yml / 环境变量中修正上述配置后重启。" + System.lineSeparator()
                + "  如确需在当前环境开启(例如联调), 显式设置 daxpay.platform.deployment.mode=DEV" + System.lineSeparator()
                + "----------------------------------------------------------";
    }
}
