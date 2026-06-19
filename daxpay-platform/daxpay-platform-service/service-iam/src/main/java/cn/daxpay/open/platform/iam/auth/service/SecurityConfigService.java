package cn.daxpay.open.platform.iam.auth.service;

import cn.daxpay.open.platform.system.entity.config.platform.security.PlatformLoginSecurityConfig;
import cn.daxpay.open.platform.system.entity.config.platform.security.PlatformPasswordPolicyConfig;
import cn.daxpay.open.platform.system.entity.config.platform.security.PlatformSessionManagementConfig;
import cn.daxpay.open.platform.system.enums.PlatformConfigTypeEnum;
import cn.daxpay.open.platform.system.service.config.SystemPlatformConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 安全策略配置服务
///
/// 读取平台安全配置
@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityConfigService {

    private final SystemPlatformConfigService systemPlatformConfigService;

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

