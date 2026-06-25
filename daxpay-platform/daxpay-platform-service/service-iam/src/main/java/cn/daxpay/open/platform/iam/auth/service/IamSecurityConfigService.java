package cn.daxpay.open.platform.iam.auth.service;

import cn.daxpay.open.platform.system.entity.config.platform.security.PlatformLoginSecurityConfig;
import cn.daxpay.open.platform.system.entity.config.platform.security.PlatformPasswordPolicyConfig;
import cn.daxpay.open.platform.system.entity.config.platform.security.PlatformSessionManagementConfig;
import cn.daxpay.open.platform.system.entity.config.platform.security.PlatformTwoFactorAuthConfig;
import cn.daxpay.open.platform.system.service.config.PlatformSecurityConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # IAM 安全配置服务
///
/// 读取平台安全配置
@Service
@RequiredArgsConstructor
public class IamSecurityConfigService {

    private final PlatformSecurityConfigService platformSecurityConfigService;

    /// 获取密码策略配置
    public PlatformPasswordPolicyConfig getPasswordPolicy() {
        return platformSecurityConfigService.getPasswordPolicyConfig();
    }

    /// 获取登录安全配置
    public PlatformLoginSecurityConfig getLoginSecurity() {
        return platformSecurityConfigService.getLoginSecurityConfig();
    }

    /// 获取会话管理配置
    public PlatformSessionManagementConfig getSessionManagement() {
        return platformSecurityConfigService.getSessionManagementConfig();
    }

    /// 获取双因素认证配置
    public PlatformTwoFactorAuthConfig getTwoFactorAuthConfig() {
        return platformSecurityConfigService.getTwoFactorAuthConfig();
    }
}

