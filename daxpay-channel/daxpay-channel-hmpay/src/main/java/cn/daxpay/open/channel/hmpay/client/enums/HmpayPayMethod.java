package cn.daxpay.open.channel.hmpay.client.enums;

/// # 河马付支付方式(主应用侧)
///
/// 与子应用 dax-pay-channel-two 的 `HmpayPayMethod` 镜像, 跨 HTTP 传输时按枚举名(name)序列化对齐。
///
/// 河马付为杉德旗下聚合支付产品, 覆盖微信/支付宝/扫码/条码。
public enum HmpayPayMethod {
    /// 聚合扫码(自动识别底层渠道)
    AGGREGATE_QR,
    /// 微信扫码
    WECHAT_QR,
    /// 支付宝扫码
    ALIPAY_QR,
    /// 微信公众号支付
    WECHAT_JSAPI,
    /// 微信小程序支付
    WECHAT_MINI,
    /// 支付宝 JSAPI
    ALIPAY_JSAPI,
    /// 条码支付(付款码 B扫C)
    BARCODE;
}
