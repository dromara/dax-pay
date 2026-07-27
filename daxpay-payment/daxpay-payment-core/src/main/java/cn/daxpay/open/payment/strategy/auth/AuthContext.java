package cn.daxpay.open.payment.strategy.auth;

/// # 认证上下文(通道应用定位信息)
///
/// 由 [AbsChannelAuthStrategy#resolveContext] 从「session 优先、param 兜底」解析得出,
/// 供各通道策略在 [AbsChannelAuthStrategy#doAuth] 中定位通道应用(appId/appSecret)。
///
/// ## 字段含义
/// - `channelMchNo`: 通道商户号(定位特约商户主数据 / 反查 mchNo)
/// - `capability`: 支付能力编码(公众号 / 小程序, 决定应用维度与 openId 类型)
/// - `channelAppId`: 显式指定的应用 AppId(可选, 优先级高于配置自动解析)
///
/// 封装此 record 是为消除各通道策略(WechatIsvAuthStrategy / WechatDirectAuthStrategy / DouyinDirectAuthStrategy)
/// 在 doAuth 开头重复的「session 字段非空判断 + param 兜底」三段相同代码。
public record AuthContext(String channelMchNo, String capability, String channelAppId) {
}
