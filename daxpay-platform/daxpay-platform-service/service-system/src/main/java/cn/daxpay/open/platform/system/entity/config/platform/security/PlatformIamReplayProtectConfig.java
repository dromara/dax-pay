package cn.daxpay.open.platform.system.entity.config.platform.security;

import lombok.Data;
import lombok.experimental.Accessors;

/// # IAM域防重放配置
///
/// 控制平台内部敏感操作（登录、注册、忘记密码、修改密码、关闭双因素认证等）的防重放校验，
/// 基于 [cn.daxpay.open.platform.capability.nonce.annotation] 体系（平台签发 nonce + 一次性消费）。
///
/// 与支付域的 [PlatformApiSecurityConfig] 区别:
/// - 本配置面向平台内部接口（IAM 域），nonce 由平台签发（挑战-应答模式）
/// - 支付配置面向开放支付 API，nonce 由商户自生成（幂等去重模式）
@Data
@Accessors(chain = true)
public class PlatformIamReplayProtectConfig {

    /// 是否启用防重放校验（默认开启，登录等接口已在使用）
    private Boolean enabled = Boolean.TRUE;

    /// Nonce有效期（秒），签发（/nonce/generate）与消费共用此 TTL
    private Integer nonceTimeoutSeconds = 300;

    /// 时间戳允许偏差（秒），请求时间戳与服务器时间差超过此值则拒绝
    private Integer timestampToleranceSeconds = 300;
}
