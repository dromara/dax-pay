package cn.daxpay.open.channel.epaylinks.client.enums;

/// # 易票联支付内容类型(主应用侧)
///
/// 与子应用 EpaylinksPayBodyType 对齐, 用于映射平台 [cn.daxpay.open.platform.core.enums.unipay.PayBodyTypeEnum]。
public enum EpaylinksPayBodyType {
    /// 二维码内容(主扫下单返回的 codeUrl)
    QR_CODE,
    /// JSAPI 调起参数(微信 wxJsapiParam / 支付宝交易号串)
    JSAPI,
    /// 跳转链接(收银台 casherUrl)
    LINK;
}
