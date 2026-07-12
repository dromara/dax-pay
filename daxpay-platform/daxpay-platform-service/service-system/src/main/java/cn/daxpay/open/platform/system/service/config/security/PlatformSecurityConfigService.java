package cn.daxpay.open.platform.system.service.config.security;

import cn.daxpay.open.platform.system.convert.config.security.PlatformSecurityConfigConvert;
import cn.daxpay.open.platform.system.entity.config.platform.security.*;
import cn.daxpay.open.platform.system.enums.PlatformConfigTypeEnum;
import cn.daxpay.open.platform.system.param.config.security.*;
import cn.daxpay.open.platform.system.result.config.security.*;
import cn.daxpay.open.platform.system.service.config.SystemPlatformConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 平台安全配置服务
///
/// 统一管理密码策略、登录安全、会话管理、双因素认证等安全类配置
@Slf4j
@Service
@RequiredArgsConstructor
public class PlatformSecurityConfigService {

    private final SystemPlatformConfigService systemConfigService;

    /// 获取密码策略配置
    public PlatformPasswordPolicyConfig getPasswordPolicyConfig() {
        return systemConfigService.getOrCreateConfig(PlatformConfigTypeEnum.SECURITY_PASSWORD_POLICY,
                PlatformPasswordPolicyConfig.class,
                new PlatformPasswordPolicyConfig());
    }

    /// 获取密码策略配置
    public PlatformPasswordPolicyConfigResult findPasswordPolicyConfig() {
        return PlatformSecurityConfigConvert.CONVERT.toPasswordPolicyResult(this.getPasswordPolicyConfig());
    }

    /// 更新密码策略配置
    public void updatePasswordPolicyConfig(PlatformPasswordPolicyConfigParam param) {
        PlatformPasswordPolicyConfig data = this.getPasswordPolicyConfig();
        PlatformSecurityConfigConvert.CONVERT.copy(param, data);
        systemConfigService.updateConfig(PlatformConfigTypeEnum.SECURITY_PASSWORD_POLICY, data);
    }

    /// 获取登录安全配置
    public PlatformLoginSecurityConfig getLoginSecurityConfig() {
        return systemConfigService.getOrCreateConfig(PlatformConfigTypeEnum.SECURITY_LOGIN,
                PlatformLoginSecurityConfig.class,
                new PlatformLoginSecurityConfig());
    }

    /// 获取登录安全配置
    public PlatformLoginSecurityConfigResult findLoginSecurityConfig() {
        return PlatformSecurityConfigConvert.CONVERT.toLoginSecurityResult(this.getLoginSecurityConfig());
    }

    /// 更新登录安全配置
    public void updateLoginSecurityConfig(PlatformLoginSecurityConfigParam param) {
        PlatformLoginSecurityConfig data = this.getLoginSecurityConfig();
        PlatformSecurityConfigConvert.CONVERT.copy(param, data);
        systemConfigService.updateConfig(PlatformConfigTypeEnum.SECURITY_LOGIN, data);
    }

    /// 获取会话管理配置
    public PlatformSessionManagementConfig getSessionManagementConfig() {
        return systemConfigService.getOrCreateConfig(PlatformConfigTypeEnum.SECURITY_SESSION,
                PlatformSessionManagementConfig.class,
                defaultSessionConfig());
    }

    /// 会话管理配置默认值: 新系统开箱即用
    private PlatformSessionManagementConfig defaultSessionConfig() {
        return new PlatformSessionManagementConfig()
                .setEnabled(true)
                .setMaxOnlineHours(72)
                .setActiveTimeoutHours(24)
                .setMaxConcurrentSessions(5)
                .setConcurrentStrategy("KICK_OLDEST")
                .setConcurrentScope("GLOBAL");
    }

    /// 获取会话管理配置
    public PlatformSessionManagementConfigResult findSessionManagementConfig() {
        return PlatformSecurityConfigConvert.CONVERT.toSessionManagementResult(this.getSessionManagementConfig());
    }

    /// 更新会话管理配置
    public void updateSessionManagementConfig(PlatformSessionManagementConfigParam param) {
        PlatformSessionManagementConfig data = this.getSessionManagementConfig();
        PlatformSecurityConfigConvert.CONVERT.copy(param, data);
        systemConfigService.updateConfig(PlatformConfigTypeEnum.SECURITY_SESSION, data);
    }

    /// 获取双因素认证配置
    public PlatformTwoFactorAuthConfig getTwoFactorAuthConfig() {
        return systemConfigService.getOrCreateConfig(PlatformConfigTypeEnum.SECURITY_TWO_FACTOR_AUTH,
                PlatformTwoFactorAuthConfig.class,
                new PlatformTwoFactorAuthConfig());
    }

    /// 获取双因素认证配置
    public PlatformTwoFactorAuthConfigResult findTwoFactorAuthConfig() {
        return PlatformSecurityConfigConvert.CONVERT.toTwoFactorAuthResult(this.getTwoFactorAuthConfig());
    }

    /// 更新双因素认证配置
    public void updateTwoFactorAuthConfig(PlatformTwoFactorAuthConfigParam param) {
        PlatformTwoFactorAuthConfig data = this.getTwoFactorAuthConfig();
        PlatformSecurityConfigConvert.CONVERT.copy(param, data);
        systemConfigService.updateConfig(PlatformConfigTypeEnum.SECURITY_TWO_FACTOR_AUTH, data);
    }
}
