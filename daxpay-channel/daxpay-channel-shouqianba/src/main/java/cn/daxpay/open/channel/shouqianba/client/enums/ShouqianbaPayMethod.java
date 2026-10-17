package cn.daxpay.open.channel.shouqianba.client.enums;

/// # 收钱吧支付方式(主应用侧)
///
/// 与子应用 ShouqianbaPayMethod 对齐: 支付宝扫码/微信扫码/云闪付扫码/微信 WAP 收银台。
public enum ShouqianbaPayMethod {
    /// 支付宝扫码(payway=2)
    ALIPAY_QR,

    /// 微信扫码(payway=3)
    WECHAT_QR,

    /// 云闪付扫码(payway=17)
    UNION_QR,

    /// 微信 WAP 收银台(wap api pro 跳转链接)
    WECHAT_H5;
}
