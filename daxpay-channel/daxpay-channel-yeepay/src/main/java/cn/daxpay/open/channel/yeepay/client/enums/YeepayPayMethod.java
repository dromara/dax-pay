package cn.daxpay.open.channel.yeepay.client.enums;

/// # 易宝支付通道支付方式(主应用侧)
///
/// 与子应用 cn.daxpay.open.channel.yeepay.enums.YeepayPayMethod 对齐。
public enum YeepayPayMethod {

    /// 聚合扫码(不指定渠道, 返回通用二维码)
    QRCODE,

    /// 微信扫码(主扫)
    WECHAT_QR,

    /// 支付宝扫码(主扫)
    ALIPAY_QR,

    /// 银联扫码(主扫)
    UNION_QR,

    /// 微信 H5
    WECHAT_H5,

    /// 支付宝 H5/WAP
    ALIPAY_H5,
}
