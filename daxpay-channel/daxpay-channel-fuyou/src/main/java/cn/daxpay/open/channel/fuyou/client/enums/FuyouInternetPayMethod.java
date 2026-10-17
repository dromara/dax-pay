package cn.daxpay.open.channel.fuyou.client.enums;

/// # 富友互联网直连支付方式(主应用侧)
///
/// 对应富友「互联网支付」接口(order.fuiou)的 order_pay_type 取值组合。
public enum FuyouInternetPayMethod {

    /// 微信扫码(主扫 C 扫 B): order_pay_type=WECHAT, 返回二维码内容
    WECHAT_QR,
    /// 微信 JSAPI(公众号): order_pay_type=JSAPI, 返回调起参数
    WECHAT_JSAPI,
    /// 微信小程序: order_pay_type=LETPAY, 返回调起参数
    WECHAT_MINI,
    /// 支付宝扫码(主扫): order_pay_type=ALIPAY, 返回二维码内容
    ALIPAY_QR,
    /// 支付宝 JS(生活号/小程序): order_pay_type=FWC, 返回交易标识
    ALIPAY_JSAPI,
    /// 银联云闪付扫码(主扫): order_pay_type=UNIONPAY, 返回二维码内容
    UNION_QR,
}
