package cn.daxpay.open.channel.dougong.client.enums;

/// # 斗拱支付方式(主应用侧)
///
/// 与子应用 dax-pay-channel-two 的 `DougongPayMethod` 镜像, 跨 HTTP 传输时按枚举名(name)序列化对齐。
///
/// 斗拱为聚合支付, 一个通道覆盖微信/支付宝/银联, 通过 tradeType 区分底层渠道。
public enum DougongPayMethod {
    /// 微信扫码(tradeType=T_NATIVE)
    WECHAT_QR,
    /// 支付宝扫码(tradeType=A_NATIVE)
    ALIPAY_QR,
    /// 银联扫码(tradeType=U_NATIVE)
    UNION_QR,
    /// 微信公众号支付(tradeType=T_JSAPI)
    WECHAT_JSAPI,
    /// 微信小程序支付(tradeType=T_MINIAPP)
    WECHAT_MINI,
    /// 支付宝 JSAPI(tradeType=A_JSAPI)
    ALIPAY_JSAPI;
}
