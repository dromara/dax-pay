package cn.daxpay.open.channel.hkrt.client.enums;

/// # 海科融通支付方式(主应用侧, 与子应用镜像)
///
/// 海科融通的支付方式由 method 单独决定, 不像拉卡拉需要 accountType + transType 三要素。
public enum HkrtPayMethod {
    /// 微信 JSAPI(公众号 / 小程序)
    WECHAT_JSAPI,
    /// 支付宝扫码(当面付二维码)
    ALIPAY_QR,
    /// 支付宝 JSAPI(生活号 / 小程序)
    ALIPAY_JSAPI,
    /// 银联二维码(云闪付)
    UNION_QR,
    /// 条码支付(微信 / 支付宝 / 银联付款码被扫)
    BARCODE;
}
