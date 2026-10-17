package cn.daxpay.open.channel.yisheng.client.enums;

/// # 易生支付方式
///
/// 对应易生两类下单接口(service), 由支付类型(pay_type)进一步区分底层渠道与形态。
public enum YishengPayMethod {
    /// 扫码支付(easypay.qrcode.pay.push, 主扫返回二维码)
    QRCODE,
    /// JSAPI 支付(easypay.js.pay.push, 返回调起参数)
    JSAPI
}
