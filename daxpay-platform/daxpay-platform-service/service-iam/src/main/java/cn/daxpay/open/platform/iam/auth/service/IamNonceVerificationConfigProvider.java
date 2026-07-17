package cn.daxpay.open.platform.iam.auth.service;

import cn.daxpay.open.platform.capability.nonce.config.NonceVerificationConfigProvider;
import cn.daxpay.open.platform.system.entity.config.platform.security.PlatformIamReplayProtectConfig;
import cn.daxpay.open.platform.system.service.config.security.PlatformSecurityConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # IAM域 Nonce 防重放配置提供者
///
/// 实现 capability-nonce 的 [NonceVerificationConfigProvider] 接口（依赖倒置），
/// 从 [PlatformSecurityConfigService] 读取 IAM 域防重放配置，
/// 供 [cn.daxpay.open.platform.capability.nonce.aop.NonceVerificationAspect] 消费。
///
/// 默认值策略: 配置缺失时保持启用（登录接口已在使用），nonce 有效期/时间戳容差回退 300 秒。
@Service
@RequiredArgsConstructor
public class IamNonceVerificationConfigProvider implements NonceVerificationConfigProvider {

    private final PlatformSecurityConfigService platformSecurityConfigService;

    @Override
    public boolean isEnabled() {
        Boolean enabled = platformSecurityConfigService.getIamReplayProtectConfig().getEnabled();
        // 默认启用，保持登录接口现有行为
        return enabled == null || enabled;
    }

    @Override
    public int getNonceTimeoutSeconds() {
        PlatformIamReplayProtectConfig config = platformSecurityConfigService.getIamReplayProtectConfig();
        Integer timeout = config.getNonceTimeoutSeconds();
        return timeout == null || timeout < 1 ? 300 : timeout;
    }

    @Override
    public int getTimestampToleranceSeconds() {
        PlatformIamReplayProtectConfig config = platformSecurityConfigService.getIamReplayProtectConfig();
        Integer tolerance = config.getTimestampToleranceSeconds();
        return tolerance == null || tolerance < 1 ? 300 : tolerance;
    }
}
