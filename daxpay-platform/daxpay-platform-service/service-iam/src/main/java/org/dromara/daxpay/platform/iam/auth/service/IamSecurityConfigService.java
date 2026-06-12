package org.dromara.daxpay.platform.iam.auth.service;

import org.dromara.daxpay.platform.system.entity.config.platform.security.PlatformLoginSecurityConfig;
import org.dromara.daxpay.platform.system.entity.config.platform.security.PlatformPasswordPolicyConfig;
import org.dromara.daxpay.platform.system.entity.config.platform.security.PlatformSessionManagementConfig;
import org.dromara.daxpay.platform.system.service.config.PlatformConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # IAM 安全配置服务
///
/// 支持平台配置与服务商配置的合并读取
@Service
@RequiredArgsConstructor
public class IamSecurityConfigService {

    private final PlatformConfigService platformConfigService;

    private final SecurityConfigService securityConfigService;

    /// 获取密码策略配置（平台配置）
    public PlatformPasswordPolicyConfig getPasswordPolicy() {
        return platformConfigService.getPasswordPolicyConfig();
    }

    /// 获取密码策略配置（支持服务商配置）
    /// @param isvNo 服务商号，为空则返回平台配置
    public PlatformPasswordPolicyConfig getPasswordPolicy(String isvNo) {
        if (StrUtil.isBlank(isvNo)) {
            return platformConfigService.getPasswordPolicyConfig();
        }
        return securityConfigService.getPasswordPolicy(isvNo);
    }

    /// 获取登录安全配置（平台配置）
    public PlatformLoginSecurityConfig getLoginSecurity() {
        return platformConfigService.getLoginSecurityConfig();
    }

    /// 获取登录安全配置（支持服务商配置）
    /// @param isvNo 服务商号，为空则返回平台配置
    public PlatformLoginSecurityConfig getLoginSecurity(String isvNo) {
        if (StrUtil.isBlank(isvNo)) {
            return platformConfigService.getLoginSecurityConfig();
        }
        return securityConfigService.getLoginSecurity(isvNo);
    }

    /// 获取会话管理配置
    public PlatformSessionManagementConfig getSessionManagement() {
        return platformConfigService.getSessionManagementConfig();
    }
}

