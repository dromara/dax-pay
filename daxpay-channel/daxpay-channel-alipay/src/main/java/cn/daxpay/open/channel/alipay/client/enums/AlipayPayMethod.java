package cn.daxpay.open.channel.alipay.client.enums;

/// # 支付宝支付方式
///
/// 与子应用 dax-pay-channel-one 的 `AlipayPayMethod` 镜像, 跨 HTTP 传输时按枚举名(name)序列化对齐。
public enum AlipayPayMethod {
    /// 手机网站支付(QUICK_WAP_WAY)
    WAP,
    /// APP 支付(QUICK_MSECURITY_PAY)
    APP,
    /// 电脑网站支付(FAST_INSTANT_TRADE_PAY)
    PC,
    /// 扫码预下单(precreate)
    QR,
    /// 付款码支付(当面付 bar_code 场景, 同步扣款)
    BARCODE,
    /// 小程序/JSAPI 支付(JSAPI_PAY, alipay.trade.create)
    JSAPI;
}
