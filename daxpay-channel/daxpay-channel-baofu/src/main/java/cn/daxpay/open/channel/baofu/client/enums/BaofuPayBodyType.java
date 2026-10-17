package cn.daxpay.open.channel.baofu.client.enums;

/// # 宝付支付内容类型(主应用侧)
///
/// 与子应用 BaofuPayBodyType 对齐, 用于映射平台 [cn.daxpay.open.platform.core.enums.unipay.PayBodyTypeEnum]。
public enum BaofuPayBodyType {
    /// 二维码内容(聚合收银台 token 链接 / ALIPAY_NATIVE qr_code / QUICK_PASS_NATIVE 二维码)
    QR_CODE,
    /// JSAPI 调起参数(WECHAT_JSAPI/MINI 的 wc_pay_data JSON)
    JSAPI,
    /// 渠道交易号标识(ALIPAY_JSAPI 的 trade_no)
    IDENTIFIER,
    /// 跳转链接(QUICK_PASS_NATIVE_JS 的 redirectUrl)
    LINK;
}
