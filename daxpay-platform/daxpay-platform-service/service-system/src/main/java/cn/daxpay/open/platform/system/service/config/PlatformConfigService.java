package cn.daxpay.open.platform.system.service.config;

import cn.daxpay.open.platform.system.convert.PlatformConfigConvert;
import cn.daxpay.open.platform.system.entity.config.platform.PlatformOssConfig;
import cn.daxpay.open.platform.system.entity.config.platform.security.*;
import cn.daxpay.open.platform.system.enums.EncryptPlatformConfigTypeEnum;
import cn.daxpay.open.platform.system.enums.PlatformConfigTypeEnum;
import cn.daxpay.open.platform.system.param.config.*;
import cn.daxpay.open.platform.system.result.config.platform.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 平台配置
///
@Slf4j
@Service
@RequiredArgsConstructor
public class PlatformConfigService {

    private final SystemPlatformConfigService systemConfigService;

    private final SystemPlatformEncryptConfigService encryptConfigService;

    /// 获取密码策略配置
    public PlatformPasswordPolicyConfig getPasswordPolicyConfig() {
        return systemConfigService.getOrCreateConfig(PlatformConfigTypeEnum.SECURITY_PASSWORD_POLICY,
                PlatformPasswordPolicyConfig.class,
                new PlatformPasswordPolicyConfig());
    }

    /// 获取密码策略配置
    public PlatformPasswordPolicyConfigResult findPasswordPolicyConfig() {
        return PlatformConfigConvert.CONVERT.toPasswordPolicyResult(this.getPasswordPolicyConfig());
    }

    /// 更新密码策略配置
    public void updatePasswordPolicyConfig(PlatformPasswordPolicyConfigParam param) {
        PlatformPasswordPolicyConfig data = this.getPasswordPolicyConfig();
        PlatformConfigConvert.CONVERT.copy(param, data);
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
        return PlatformConfigConvert.CONVERT.toLoginSecurityResult(this.getLoginSecurityConfig());
    }

    /// 更新登录安全配置
    public void updateLoginSecurityConfig(PlatformLoginSecurityConfigParam param) {
        PlatformLoginSecurityConfig data = this.getLoginSecurityConfig();
        PlatformConfigConvert.CONVERT.copy(param, data);
        systemConfigService.updateConfig(PlatformConfigTypeEnum.SECURITY_LOGIN, data);
    }

    /// 获取会话管理配置
    public PlatformSessionManagementConfig getSessionManagementConfig() {
        return systemConfigService.getOrCreateConfig(PlatformConfigTypeEnum.SECURITY_SESSION,
                PlatformSessionManagementConfig.class,
                new PlatformSessionManagementConfig());
    }

    /// 获取会话管理配置
    public PlatformSessionManagementConfigResult findSessionManagementConfig() {
        return PlatformConfigConvert.CONVERT.toSessionManagementResult(this.getSessionManagementConfig());
    }

    /// 更新会话管理配置
    public void updateSessionManagementConfig(PlatformSessionManagementConfigParam param) {
        PlatformSessionManagementConfig data = this.getSessionManagementConfig();
        PlatformConfigConvert.CONVERT.copy(param, data);
        systemConfigService.updateConfig(PlatformConfigTypeEnum.SECURITY_SESSION, data);
    }

    /// 获取异常登录检测配置
    public PlatformAnomalyDetectionConfig getAnomalyDetectionConfig() {
        return systemConfigService.getOrCreateConfig(PlatformConfigTypeEnum.ANOMALY_DETECTION,
                PlatformAnomalyDetectionConfig.class,
                new PlatformAnomalyDetectionConfig());
    }

    /// 获取异常登录检测配置
    public PlatformAnomalyDetectionConfigResult findAnomalyDetectionConfig() {
        return PlatformConfigConvert.CONVERT.toAnomalyDetectionResult(this.getAnomalyDetectionConfig());
    }

    /// 更新异常登录检测配置
    public void updateAnomalyDetectionConfig(PlatformAnomalyDetectionConfigParam param) {
        PlatformAnomalyDetectionConfig data = this.getAnomalyDetectionConfig();
        PlatformConfigConvert.CONVERT.copy(param, data);
        systemConfigService.updateConfig(PlatformConfigTypeEnum.ANOMALY_DETECTION, data);
    }

    /// 获取双因素认证配置
    public PlatformTwoFactorAuthConfig getTwoFactorAuthConfig() {
        return systemConfigService.getOrCreateConfig(PlatformConfigTypeEnum.SECURITY_TWO_FACTOR_AUTH,
                PlatformTwoFactorAuthConfig.class,
                new PlatformTwoFactorAuthConfig());
    }

    /// 获取双因素认证配置
    public PlatformTwoFactorAuthConfigResult findTwoFactorAuthConfig() {
        return PlatformConfigConvert.CONVERT.toTwoFactorAuthResult(this.getTwoFactorAuthConfig());
    }

    /// 更新双因素认证配置
    public void updateTwoFactorAuthConfig(PlatformTwoFactorAuthConfigParam param) {
        PlatformTwoFactorAuthConfig data = this.getTwoFactorAuthConfig();
        PlatformConfigConvert.CONVERT.copy(param, data);
        systemConfigService.updateConfig(PlatformConfigTypeEnum.SECURITY_TWO_FACTOR_AUTH, data);
    }

    /// 获取OSS配置
    public PlatformOssConfig getOssConfig() {
        return encryptConfigService.getOrCreateConfig(EncryptPlatformConfigTypeEnum.OSS,
                PlatformOssConfig.class,
                new PlatformOssConfig());
    }

    /// 获取OSS配置
    public PlatformOssConfigResult findOssConfig() {
        return PlatformConfigConvert.CONVERT.toOssResult(this.getOssConfig());
    }

    /// 更新OSS配置
    public void updateOssConfig(PlatformOssConfigParam param) {
        PlatformOssConfig data = this.getOssConfig();
        PlatformConfigConvert.CONVERT.copy(param, data);
        encryptConfigService.updateConfig(EncryptPlatformConfigTypeEnum.OSS, data);
    }

}
