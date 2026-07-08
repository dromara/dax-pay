package cn.daxpay.open.channel.fuyou.client.enums;

/// # 富友支付方式(主应用侧)
public enum FuyouPayMethod {

    /// 微信扫码(主扫 C 扫 B)
    WECHAT_QR,
    /// 微信 JSAPI(公众号)
    WECHAT_JSAPI,
    /// 微信小程序
    WECHAT_MINI,
    /// 支付宝扫码(主扫)
    ALIPAY_QR,
    /// 支付宝 JSAPI(生活号)
    ALIPAY_JSAPI,
    /// 支付宝小程序
    ALIPAY_MINI,
    /// 银联扫码(主扫)
    UNION_QR,
    /// 付款码(被扫 B 扫 C)
    BARCODE,
}
