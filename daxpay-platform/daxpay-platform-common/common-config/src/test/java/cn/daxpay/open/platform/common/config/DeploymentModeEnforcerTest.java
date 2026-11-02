package cn.daxpay.open.platform.common.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/// # 生产部署模式启动期校验器测试
///
/// 覆盖 `allow-admin-in-prod` 单点放行语义: 为 true 时只跳过"超级管理员登录"一项,
/// 其余生产校验照常; 未配置/为 false 时维持原行为(超管开启即拒绝启动)。
class DeploymentModeEnforcerTest {

    private final DeploymentModeEnforcer enforcer = new DeploymentModeEnforcer();

    @Test
    @DisplayName("allow-admin-in-prod=true 时跳过超管校验, 其余合规配置正常通过")
    void shouldSkipAdminCheckWhenAllowed() {
        Map<String, Object> props = prodSafeProps();
        props.put("daxpay.platform.starter.auth.enable-admin", "true");
        props.put("daxpay.platform.starter.auth.allow-admin-in-prod", "true");

        assertDoesNotThrow(() -> enforcer.postProcessEnvironment(env(props), new SpringApplication()));
    }

    @Test
    @DisplayName("allow-admin-in-prod=true 时其余违规仍报错, 且失败信息不含超管违规项")
    void shouldStillFailOnOtherViolationsWhenAdminAllowed() {
        Map<String, Object> props = prodSafeProps();
        props.put("daxpay.platform.starter.auth.enable-admin", "true");
        props.put("daxpay.platform.starter.auth.allow-admin-in-prod", "true");
        props.put("daxpay.platform.common.exception.show-full-message", "true");

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> enforcer.postProcessEnvironment(env(props), new SpringApplication()));
        String message = ex.getMessage();
        assertTrue(message.contains("show-full-message"), "其余违规项必须仍被收集: " + message);
        assertFalse(message.contains("daxpay.platform.starter.auth.enable-admin = "),
                "超管一项应已放行, 不应出现在违规列表中: " + message);
        assertTrue(message.contains("allow-admin-in-prod=true"), "失败信息应注明超管已放行: " + message);
    }

    @Test
    @DisplayName("未配置 allow-admin-in-prod 时超管开启照旧拒绝启动")
    void shouldRejectAdminWhenNotAllowed() {
        Map<String, Object> props = prodSafeProps();
        props.put("daxpay.platform.starter.auth.enable-admin", "true");

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> enforcer.postProcessEnvironment(env(props), new SpringApplication()));
        assertTrue(ex.getMessage().contains("daxpay.platform.starter.auth.enable-admin = "),
                "超管违规项应出现在失败信息中: " + ex.getMessage());
    }

    @Test
    @DisplayName("allow-admin-in-prod=false 等价于未配置")
    void shouldRejectAdminWhenExplicitlyDisabled() {
        Map<String, Object> props = prodSafeProps();
        props.put("daxpay.platform.starter.auth.enable-admin", "true");
        props.put("daxpay.platform.starter.auth.allow-admin-in-prod", "false");

        assertThrows(IllegalStateException.class,
                () -> enforcer.postProcessEnvironment(env(props), new SpringApplication()));
    }

    /// 一份 PROD 下除超管外全部合规的属性集合(逐项覆盖 8 项校验)
    private Map<String, Object> prodSafeProps() {
        Map<String, Object> props = new HashMap<>();
        props.put("daxpay.platform.deployment.mode", "PROD");
        props.put("daxpay.platform.config.sandbox-enabled", "false");
        props.put("daxpay.platform.common.exception.show-full-message", "false");
        props.put("springdoc.api-docs.enabled", "false");
        props.put("springdoc.swagger-ui.enabled", "false");
        props.put("daxpay.platform.common.spring.cors.enable", "false");
        props.put("management.endpoints.web.exposure.include", "health,info,metrics");
        props.put("management.endpoint.health.show-details", "never");
        return props;
    }

    private StandardEnvironment env(Map<String, Object> props) {
        StandardEnvironment environment = new StandardEnvironment();
        // addFirst 保证测试属性优先于系统环境变量(避免宿主环境的 SPRING_* 干扰)
        environment.getPropertySources().addFirst(new MapPropertySource("test", props));
        return environment;
    }
}
