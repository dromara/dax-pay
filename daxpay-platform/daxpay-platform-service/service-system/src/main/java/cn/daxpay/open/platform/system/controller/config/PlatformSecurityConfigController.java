package cn.daxpay.open.platform.system.controller.config;

import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.system.param.config.security.*;
import cn.daxpay.open.platform.system.result.config.platform.*;
import cn.daxpay.open.platform.system.service.config.PlatformSecurityConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/// # 平台安全配置
///
/// 管理密码策略、登录安全、会话管理、异常登录检测、双因素认证等安全类配置
@PermCode(menuCode = "system:security:config")
@Validated
@Tag(name = "平台安全配置")
@RestController
@RequestMapping("/platform/config/security")
@RequiredArgsConstructor
public class PlatformSecurityConfigController {
    private final PlatformSecurityConfigService platformSecurityConfigService;

    @PermCode(code = "security:view", nameCn = "安全配置查看", nameEn = "Security View")
    @Operation(summary = "获取密码策略配置")
    @GetMapping("/password-policy/get")
    public Result<PlatformPasswordPolicyConfigResult> getPasswordPolicyConfig() {
        return Res.ok(platformSecurityConfigService.findPasswordPolicyConfig());
    }

    @IgnoreAuth
    @Operation(summary = "获取密码策略校验配置（供前端校验使用）")
    @GetMapping("/password-policy/validate-config")
    public Result<PlatformPasswordPolicyConfigResult> getPasswordPolicyValidateConfig() {
        return Res.ok(platformSecurityConfigService.findPasswordPolicyConfig());
    }

    @PermCode(code = "security:manage", nameCn = "安全配置管理", nameEn = "Security Manage")
    @Operation(summary = "更新密码策略配置")
    @PostMapping("/password-policy/update")
    public Result<Void> updatePasswordPolicyConfig(@RequestBody @Validated PlatformPasswordPolicyConfigParam param) {
        platformSecurityConfigService.updatePasswordPolicyConfig(param);
        return Res.ok();
    }

    @PermCode(code = "security:view", nameCn = "安全配置查看", nameEn = "Security View")
    @Operation(summary = "获取登录安全配置")
    @GetMapping("/login/get")
    public Result<PlatformLoginSecurityConfigResult> getLoginSecurityConfig() {
        return Res.ok(platformSecurityConfigService.findLoginSecurityConfig());
    }

    @PermCode(code = "security:manage", nameCn = "安全配置管理", nameEn = "Security Manage")
    @Operation(summary = "更新登录安全配置")
    @PostMapping("/login/update")
    public Result<Void> updateLoginSecurityConfig(@RequestBody @Validated PlatformLoginSecurityConfigParam param) {
        platformSecurityConfigService.updateLoginSecurityConfig(param);
        return Res.ok();
    }

    @PermCode(code = "security:view", nameCn = "安全配置查看", nameEn = "Security View")
    @Operation(summary = "获取会话管理配置")
    @GetMapping("/session/get")
    public Result<PlatformSessionManagementConfigResult> getSessionManagementConfig() {
        return Res.ok(platformSecurityConfigService.findSessionManagementConfig());
    }

    @PermCode(code = "security:manage", nameCn = "安全配置管理", nameEn = "Security Manage")
    @Operation(summary = "更新会话管理配置")
    @PostMapping("/session/update")
    public Result<Void> updateSessionManagementConfig(@RequestBody @Validated PlatformSessionManagementConfigParam param) {
        platformSecurityConfigService.updateSessionManagementConfig(param);
        return Res.ok();
    }

    @PermCode(code = "security:view", nameCn = "安全配置查看", nameEn = "Security View")
    @Operation(summary = "获取异常登录检测配置")
    @GetMapping("/anomaly-detection/get")
    public Result<PlatformAnomalyDetectionConfigResult> getAnomalyDetectionConfig() {
        return Res.ok(platformSecurityConfigService.findAnomalyDetectionConfig());
    }

    @PermCode(code = "security:manage", nameCn = "安全配置管理", nameEn = "Security Manage")
    @Operation(summary = "更新异常登录检测配置")
    @PostMapping("/anomaly-detection/update")
    public Result<Void> updateAnomalyDetectionConfig(@RequestBody @Validated PlatformAnomalyDetectionConfigParam param) {
        platformSecurityConfigService.updateAnomalyDetectionConfig(param);
        return Res.ok();
    }

    @PermCode(code = "security:view", nameCn = "安全配置查看", nameEn = "Security View")
    @Operation(summary = "获取双因素认证配置")
    @GetMapping("/two-factor-auth/get")
    public Result<PlatformTwoFactorAuthConfigResult> getTwoFactorAuthConfig() {
        return Res.ok(platformSecurityConfigService.findTwoFactorAuthConfig());
    }

    @PermCode(code = "security:manage", nameCn = "安全配置管理", nameEn = "Security Manage")
    @Operation(summary = "更新双因素认证配置")
    @PostMapping("/two-factor-auth/update")
    public Result<Void> updateTwoFactorAuthConfig(@RequestBody @Validated PlatformTwoFactorAuthConfigParam param) {
        platformSecurityConfigService.updateTwoFactorAuthConfig(param);
        return Res.ok();
    }
}
