package cn.daxpay.open.channel.yizhifubj.client.enums;

/// # 一直付支付方式(主应用侧)
///
/// 与子应用 YizhifubjPayMethod 对齐: 支付宝/微信/云闪付扫码、支付宝 WAP、支付宝/微信公众号 JsApi。
public enum YizhifubjPayMethod {
    /// 支付宝扫码(SCANCODE-ALI_PAY-P2P)
    ALIPAY_QR,
    /// 微信扫码(SCANCODE-WEIXIN_PAY-P2P)
    WECHAT_QR,
    /// 云闪付扫码(SCANCODE-UNION_PAY-P2P)
    UNION_QR,
    /// 支付宝 WAP/页面跳转(ALIPAY-WAP-P2P)
    ALIPAY_WAP,
    /// 支付宝 JsApi(ALIPAY-OFFICIAL-P2P)
    ALIPAY_JSAPI,
    /// 微信公众号 JsApi(WECHAT-OFFICIAL_PAY-P2P)
    WECHAT_JSAPI,
}
