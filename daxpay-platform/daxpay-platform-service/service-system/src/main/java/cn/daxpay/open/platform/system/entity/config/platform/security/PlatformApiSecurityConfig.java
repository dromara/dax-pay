package cn.daxpay.open.platform.system.entity.config.platform.security;

import lombok.Data;
import lombok.experimental.Accessors;

/// # API安全配置
///
/// 控制开放支付接口（[cn.daxpay.open.payment.unipay.aop.PaymentVerify]）的防重放校验:
/// - Nonce 一次性消费: 防止相同请求被重放
/// - 请求时间窗口校验: 防止过期请求被重放
@Data
@Accessors(chain = true)
public class PlatformApiSecurityConfig {

    /// 是否启用 Nonce 防重放校验（商户自生成 nonceStr，平台 SETNX 一次性消费）
    private Boolean nonceVerifyEnabled = Boolean.FALSE;

    /// 是否启用请求时间窗口校验（reqTime 与服务器时间差值超过阈值则拒绝）
    private Boolean reqTimeoutEnabled = Boolean.FALSE;

    /// 请求时间窗口容差（秒），双向绝对值校验
    private Integer reqTimeoutSeconds = 300;

    /// Nonce 有效期（秒），即 Redis 缓存 TTL
    private Integer nonceTtlSeconds = 300;
}
