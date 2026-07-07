package cn.daxpay.open.channel.vbill.client.enums;

/// # 随行付支付方式(主应用侧)
///
/// 与子应用 [cn.daxpay.open.channel.vbill.enums.VbillPayMethod] 对称。
public enum VbillPayMethod {
    /// 聚合支付(统一下单, JSAPI/小程序)
    UNI_PAY,
    /// 扫码支付(主扫, 返回二维码)
    QR_CODE,
    /// 付款码支付(被扫, 同步扣款)
    BAR_CODE,
    /// 小程序收银台
    APPLET_CASHIER;
}
