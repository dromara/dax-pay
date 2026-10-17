package cn.daxpay.open.channel.sand.client.enums;

/// # 杉德支付方式(主应用侧)
///
/// 与子应用 SandPayMethod 对齐: payType/payMode 二元组路由,
/// 一期支持微信/支付宝/云闪付扫码 + 微信/支付宝 JSAPI(含小程序)。
public enum SandPayMethod {
    /// 微信扫码(WXPAY/QR)
    WECHAT_QR,
    /// 支付宝扫码(ALIPAY/QR)
    ALIPAY_QR,
    /// 云闪付扫码(CUPPAY/QR)
    UNION_QR,
    /// 微信 JSAPI/公众号(WXPAY/JSAPI)
    WECHAT_JSAPI,
    /// 微信小程序(WXPAY/MINI)
    WECHAT_MINI,
    /// 支付宝 JSAPI/小程序(ALIPAY/JSAPI)
    ALIPAY_JSAPI
}
