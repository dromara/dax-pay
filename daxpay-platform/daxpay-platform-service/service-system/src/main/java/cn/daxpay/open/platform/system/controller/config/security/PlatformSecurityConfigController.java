package cn.daxpay.open.platform.system.controller.config.security;

import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.system.param.config.security.*;
import cn.daxpay.open.platform.system.result.config.security.*;
import cn.daxpay.open.platform.system.service.config.security.PlatformSecurityConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/// # 平台安全配置
///
/// 管理密码策略、登录安全、会话管理、双因素认证等安全类配置
@PermCode(menuCode = PermCodes.System.SecurityConfig.MENU)
@Validated
@Tag(name = "平台安全配置")
@RestController
@RequestMapping("/platform/config/security")
@RequiredArgsConstructor
public class PlatformSecurityConfigController {
    private final PlatformSecurityConfigService platformSecurityConfigService;

    @PermCode(code = PermCodes.Action.VIEW)
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

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "更新密码策略配置")
    @PostMapping("/password-policy/update")
    public Result<Void> updatePasswordPolicyConfig(@RequestBody @Validated PlatformPasswordPolicyConfigParam param) {
        platformSecurityConfigService.updatePasswordPolicyConfig(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "获取登录安全配置")
    @GetMapping("/login/get")
    public Result<PlatformLoginSecurityConfigResult> getLoginSecurityConfig() {
        return Res.ok(platformSecurityConfigService.findLoginSecurityConfig());
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "更新登录安全配置")
    @PostMapping("/login/update")
    public Result<Void> updateLoginSecurityConfig(@RequestBody @Validated PlatformLoginSecurityConfigParam param) {
        platformSecurityConfigService.updateLoginSecurityConfig(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "获取会话管理配置")
    @GetMapping("/session/get")
    public Result<PlatformSessionManagementConfigResult> getSessionManagementConfig() {
        return Res.ok(platformSecurityConfigService.findSessionManagementConfig());
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "更新会话管理配置")
    @PostMapping("/session/update")
    public Result<Void> updateSessionManagementConfig(@RequestBody @Validated PlatformSessionManagementConfigParam param) {
        platformSecurityConfigService.updateSessionManagementConfig(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "获取双因素认证配置")
    @GetMapping("/two-factor-auth/get")
    public Result<PlatformTwoFactorAuthConfigResult> getTwoFactorAuthConfig() {
        return Res.ok(platformSecurityConfigService.findTwoFactorAuthConfig());
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "更新双因素认证配置")
    @PostMapping("/two-factor-auth/update")
    public Result<Void> updateTwoFactorAuthConfig(@RequestBody @Validated PlatformTwoFactorAuthConfigParam param) {
        platformSecurityConfigService.updateTwoFactorAuthConfig(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "获取通行密钥配置")
    @GetMapping("/webauthn/get")
    public Result<PlatformWebAuthnConfigResult> getWebAuthnConfig() {
        return Res.ok(platformSecurityConfigService.findWebAuthnConfig());
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "更新通行密钥配置")
    @PostMapping("/webauthn/update")
    public Result<Void> updateWebAuthnConfig(@RequestBody @Validated PlatformWebAuthnConfigParam param) {
        platformSecurityConfigService.updateWebAuthnConfig(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "获取IAM域防重放配置")
    @GetMapping("/iam-replay-protect/get")
    public Result<PlatformIamReplayProtectConfigResult> getIamReplayProtectConfig() {
        return Res.ok(platformSecurityConfigService.findIamReplayProtectConfig());
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "更新IAM域防重放配置")
    @PostMapping("/iam-replay-protect/update")
    public Result<Void> updateIamReplayProtectConfig(@RequestBody @Validated PlatformIamReplayProtectConfigParam param) {
        platformSecurityConfigService.updateIamReplayProtectConfig(param);
        return Res.ok();
    }
}
