package cn.daxpay.open.platform.capability.nonce.config;

/// # Nonce防重放校验配置提供者（依赖倒置接口）
///
/// capability-nonce 作为底层能力模块，不依赖上层配置存储（service-system）。
/// 通过此接口由上层（如 service-iam）实现配置注入，实现依赖倒置。
///
/// 实现方应注册为 Spring Bean，capability-nonce 通过
/// [org.springframework.beans.factory.ObjectProvider] 做 optional 注入:
/// - **有实现 bean**: 从实现读取配置，实现全局可配置
/// - **无实现 bean**: 走 [cn.daxpay.open.platform.core.annotation.NonceVerification] 注解默认值，向后兼容
///
/// 配置优先级: provider（全局配置）> 注解参数（方法级覆盖）> 默认值 300
public interface NonceVerificationConfigProvider {

    /// 是否启用防重放校验（关闭后所有 @NonceVerification 标记的接口跳过校验）
    boolean isEnabled();

    /// Nonce有效期（秒），签发（/nonce/generate）与消费共用此 TTL
    int getNonceTimeoutSeconds();

    /// 时间戳允许偏差（秒），请求时间戳与服务器时间差超过此值则拒绝
    int getTimestampToleranceSeconds();
}
