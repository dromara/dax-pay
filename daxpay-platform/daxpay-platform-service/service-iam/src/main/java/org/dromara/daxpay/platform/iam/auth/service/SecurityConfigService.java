package org.dromara.daxpay.platform.iam.auth.service;

import org.dromara.daxpay.platform.system.entity.config.platform.security.PlatformLoginSecurityConfig;
import org.dromara.daxpay.platform.system.entity.config.platform.security.PlatformPasswordPolicyConfig;
import org.dromara.daxpay.platform.system.entity.config.platform.security.PlatformSessionManagementConfig;
import org.dromara.daxpay.platform.system.enums.PlatformConfigTypeEnum;
import org.dromara.daxpay.platform.system.service.config.SystemPlatformConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 安全策略配置服务
///
/// 支持平台配置与服务商配置的合并读取
@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityConfigService {

    private final SystemPlatformConfigService systemPlatformConfigService;/// 获取密码策略配置
    /// @param isvNo 服务商号，为空则返回平台配置
    public PlatformPasswordPolicyConfig getPasswordPolicy(String isvNo) {
        PlatformPasswordPolicyConfig platformConfig = this.getPlatformPasswordPolicy();
        if (StrUtil.isBlank(isvNo)) {
            return platformConfig;
        }

        PlatformPasswordPolicyConfig isvConfig = null;
        if (isvConfig == null) {
            return platformConfig;
        }

        return isvConfig;
    }

    /// 获取登录安全配置
    /// @param isvNo 服务商号，为空则返回平台配置
    public PlatformLoginSecurityConfig getLoginSecurity(String isvNo) {
        PlatformLoginSecurityConfig platformConfig = this.getPlatformLoginSecurity();
        if (StrUtil.isBlank(isvNo)) {
            return platformConfig;
        }

        PlatformLoginSecurityConfig isvConfig = null;
        if (isvConfig == null) {
            return platformConfig;
        }

        return isvConfig;
    }

    /// 获取会话管理配置
    /// @param isvNo 服务商号，为空则返回平台配置
    public PlatformSessionManagementConfig getSessionManagement(String isvNo) {
        PlatformSessionManagementConfig platformConfig = this.getPlatformSessionManagement();
        if (StrUtil.isBlank(isvNo)) {
            return platformConfig;
        }

        PlatformSessionManagementConfig isvConfig = null;
        if (isvConfig == null) {
            return platformConfig;
        }

        return isvConfig;
    }

    /// 获取平台密码策略配置
    public PlatformPasswordPolicyConfig getPlatformPasswordPolicy() {
        return systemPlatformConfigService.getOrCreateConfig(
                PlatformConfigTypeEnum.SECURITY_PASSWORD_POLICY,
                PlatformPasswordPolicyConfig.class,
                new PlatformPasswordPolicyConfig());
    }

    /// 获取平台登录安全配置
    public PlatformLoginSecurityConfig getPlatformLoginSecurity() {
        return systemPlatformConfigService.getOrCreateConfig(
                PlatformConfigTypeEnum.SECURITY_LOGIN,
                PlatformLoginSecurityConfig.class,
                new PlatformLoginSecurityConfig());
    }

    /// 获取平台会话管理配置
    public PlatformSessionManagementConfig getPlatformSessionManagement() {
        return systemPlatformConfigService.getOrCreateConfig(
                PlatformConfigTypeEnum.SECURITY_SESSION,
                PlatformSessionManagementConfig.class,
                new PlatformSessionManagementConfig());
    }
}

