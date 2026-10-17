package cn.daxpay.open.channel.umfpay.client.enums;

/// # 联动优势支付方式(主应用侧)
///
/// 与子应用 cn.daxpay.open.channel.umfpay.enums.UmfpayPayMethod 枚举项保持一致(序列化互通):
/// - 扫码族: active_scancode_order_new, 按 scancode_type(WECHAT/ALIPAY/UNION)指定底层渠道
/// - 公众号: publicnumber_and_verticalcode(is_public_number=Y), GET 跳转链接
public enum UmfpayPayMethod {
    /// 微信扫码(scancode_type=WECHAT)
    WECHAT_QR,
    /// 支付宝扫码(scancode_type=ALIPAY)
    ALIPAY_QR,
    /// 云闪付扫码(scancode_type=UNION)
    UNION_QR,
    /// 微信公众号支付(publicnumber_and_verticalcode, GET 跳转链接)
    WECHAT_PUBLIC;
}
