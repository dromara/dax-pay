package cn.daxpay.open.platform.system.controller.config;

import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.system.param.config.*;
import cn.daxpay.open.platform.system.result.config.platform.*;
import cn.daxpay.open.platform.system.service.config.PlatformConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/// # 平台配置
///
@PermCode(menuCode = "system:security:config")
@Validated
@Tag(name = "平台配置")
@RestController
@RequestMapping("/platform/config")
@RequiredArgsConstructor
public class PlatformConfigController {
    private final PlatformConfigService platformConfigService;

    @PermCode(code = "security:view", nameCn = "安全配置查看", nameEn = "Security View")
    @Operation(summary = "获取密码策略配置")
    @GetMapping("/security/password-policy/get")
    public Result<PlatformPasswordPolicyConfigResult> getPasswordPolicyConfig() {
        return Res.ok(platformConfigService.findPasswordPolicyConfig());
    }

    @IgnoreAuth
    @Operation(summary = "获取密码策略校验配置（供前端校验使用）")
    @GetMapping("/security/password-policy/validate-config")
    public Result<PlatformPasswordPolicyConfigResult> getPasswordPolicyValidateConfig() {
        return Res.ok(platformConfigService.findPasswordPolicyConfig());
    }

    @PermCode(code = "security:manage", nameCn = "安全配置管理", nameEn = "Security Manage")
    @Operation(summary = "更新密码策略配置")
    @PostMapping("/security/password-policy/update")
    public Result<Void> updatePasswordPolicyConfig(@RequestBody @Validated PlatformPasswordPolicyConfigParam param) {
        platformConfigService.updatePasswordPolicyConfig(param);
        return Res.ok();
    }

    @PermCode(code = "security:view", nameCn = "安全配置查看", nameEn = "Security View")
    @Operation(summary = "获取登录安全配置")
    @GetMapping("/security/login/get")
    public Result<PlatformLoginSecurityConfigResult> getLoginSecurityConfig() {
        return Res.ok(platformConfigService.findLoginSecurityConfig());
    }

    @PermCode(code = "security:manage", nameCn = "安全配置管理", nameEn = "Security Manage")
    @Operation(summary = "更新登录安全配置")
    @PostMapping("/security/login/update")
    public Result<Void> updateLoginSecurityConfig(@RequestBody @Validated PlatformLoginSecurityConfigParam param) {
        platformConfigService.updateLoginSecurityConfig(param);
        return Res.ok();
    }

    @PermCode(code = "security:view", nameCn = "安全配置查看", nameEn = "Security View")
    @Operation(summary = "获取会话管理配置")
    @GetMapping("/security/session/get")
    public Result<PlatformSessionManagementConfigResult> getSessionManagementConfig() {
        return Res.ok(platformConfigService.findSessionManagementConfig());
    }

    @PermCode(code = "security:manage", nameCn = "安全配置管理", nameEn = "Security Manage")
    @Operation(summary = "更新会话管理配置")
    @PostMapping("/security/session/update")
    public Result<Void> updateSessionManagementConfig(@RequestBody @Validated PlatformSessionManagementConfigParam param) {
        platformConfigService.updateSessionManagementConfig(param);
        return Res.ok();
    }

    @PermCode(code = "security:view", nameCn = "安全配置查看", nameEn = "Security View")
    @Operation(summary = "获取异常登录检测配置")
    @GetMapping("/security/anomaly-detection/get")
    public Result<PlatformAnomalyDetectionConfigResult> getAnomalyDetectionConfig() {
        return Res.ok(platformConfigService.findAnomalyDetectionConfig());
    }

    @PermCode(code = "security:manage", nameCn = "安全配置管理", nameEn = "Security Manage")
    @Operation(summary = "更新异常登录检测配置")
    @PostMapping("/security/anomaly-detection/update")
    public Result<Void> updateAnomalyDetectionConfig(@RequestBody @Validated PlatformAnomalyDetectionConfigParam param) {
        platformConfigService.updateAnomalyDetectionConfig(param);
        return Res.ok();
    }

    @PermCode(code = "security:view", nameCn = "安全配置查看", nameEn = "Security View")
    @Operation(summary = "获取双因素认证配置")
    @GetMapping("/security/two-factor-auth/get")
    public Result<PlatformTwoFactorAuthConfigResult> getTwoFactorAuthConfig() {
        return Res.ok(platformConfigService.findTwoFactorAuthConfig());
    }

    @PermCode(code = "security:manage", nameCn = "安全配置管理", nameEn = "Security Manage")
    @Operation(summary = "更新双因素认证配置")
    @PostMapping("/security/two-factor-auth/update")
    public Result<Void> updateTwoFactorAuthConfig(@RequestBody @Validated PlatformTwoFactorAuthConfigParam param) {
        platformConfigService.updateTwoFactorAuthConfig(param);
        return Res.ok();
    }

    @PermCode(code = "security:view", nameCn = "安全配置查看", nameEn = "Security View")
    @Operation(summary = "获取OSS配置")
    @GetMapping("/oss/get")
    public Result<PlatformOssConfigResult> getOssConfig() {
        return Res.ok(platformConfigService.findOssConfig());
    }

    @PermCode(code = "security:manage", nameCn = "安全配置管理", nameEn = "Security Manage")
    @Operation(summary = "更新OSS配置")
    @PostMapping("/oss/update")
    public Result<Void> updateOssConfig(@RequestBody @Validated PlatformOssConfigParam param) {
        platformConfigService.updateOssConfig(param);
        return Res.ok();
    }

}
