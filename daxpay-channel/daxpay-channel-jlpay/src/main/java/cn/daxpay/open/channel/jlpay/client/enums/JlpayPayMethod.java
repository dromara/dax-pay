package cn.daxpay.open.channel.jlpay.client.enums;

/// # 嘉联支付方式
///
/// 嘉联开放平台聚合通道通过两个接口承载一期支付方式:
/// - QRCODE: 扫码支付(主扫), 走 `/open/trans/qrcodepay`, 按 pay_type(wxpay/alipay/unionpay)指定底层渠道
/// - MICROPAY: 付款码支付(被扫), 走 `/open/trans/micropay`, 凭 auth_code 由嘉联自动识别渠道
public enum JlpayPayMethod {
    /// 扫码支付(主扫, /open/trans/qrcodepay)
    QRCODE,
    /// 付款码支付(被扫, /open/trans/micropay)
    MICROPAY;
}
