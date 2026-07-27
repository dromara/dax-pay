package cn.daxpay.open.payment.auth.merchant;

/// # 认证上下文(通道应用定位信息)
///
/// 由 [AbsProductAuthStrategy#resolveContext] 从「session 优先、param 兜底」解析得出,
/// 供各通道策略在 [AbsProductAuthStrategy#doAuth] 中定位通道应用(appId/appSecret)。
///
/// ## 字段含义
/// - `channelMchNo`: 通道商户号(定位特约商户主数据 / 反查 mchNo)
/// - `capability`: 支付能力编码(公众号 / 小程序, 决定应用维度与 openId 类型)
/// - `channelAppId`: 显式指定的应用 AppId(可选, 优先级高于配置自动解析)
public record ProductAuthContext(String channelMchNo, String capability, String channelAppId) {
}
