package cn.daxpay.open.channel.wechat.client.enums;

/// # 微信支付方式
///
/// 与子应用 dax-pay-channel-one 的 `WechatPayMethod` 镜像, 跨 HTTP 传输时按枚举名(name)序列化对齐。
public enum WechatPayMethod {
    /// 扫码支付(NATIVE)
    NATIVE,
    /// 公众号支付(JSAPI)
    JSAPI,
    /// 小程序支付(JSAPI 接口)
    MINI,
    /// APP 支付
    APP,
    /// H5 支付
    H5,
    /// 付款码支付(V2 micropay)
    MICROPAY;
}
