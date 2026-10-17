package cn.daxpay.open.channel.baofu.client.enums;

/// # 宝付支付方式(主应用侧)
///
/// 与子应用 BaofuPayMethod 对齐, 由主应用按平台支付方式([cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum])
/// 映射后下发。
public enum BaofuPayMethod {
    /// 聚合收银台(主扫, pre_unified_order 返回 token 收银台链接)
    AGGREGATE_QR,
    /// 微信 JSAPI(公众号/小程序, unified_order 返回 wc_pay_data 调起参数)
    WECHAT_JSAPI,
    /// 微信付款码(被扫, unified_order WECHAT_MICROPAY)
    WECHAT_BARCODE,
    /// 支付宝扫码(主扫, unified_order ALIPAY_NATIVE 返回 qr_code)
    ALIPAY_QR,
    /// 支付宝 JSAPI(unified_order ALIPAY_JSAPI 返回 trade_no 标识)
    ALIPAY_JSAPI,
    /// 支付宝付款码(被扫, unified_order ALIPAY_MICROPAY)
    ALIPAY_BARCODE,
    /// 云闪付扫码(主扫, unified_order QUICK_PASS_NATIVE 返回二维码)
    UNION_QR,
    /// 云闪付 JS(unified_order QUICK_PASS_NATIVE_JS 返回 redirectUrl 跳转链接)
    UNION_JSAPI;
}
