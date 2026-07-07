package cn.daxpay.open.channel.vbill.client.enums;

/// # 随行付支付内容类型(主应用侧)
///
/// 与子应用 [cn.daxpay.open.channel.vbill.enums.VbillPayBodyType] 对称。
public enum VbillPayBodyType {
    /// 跳转链接(银联 JSAPI redirectUrl)
    LINK,
    /// 二维码内容(扫码支付 payUrl)
    QR_CODE,
    /// JSAPI/小程序调起参数 JSON(微信/收银台调起参数)
    JSAPI,
    /// 通用标识码(支付宝 JSAPI source)
    IDENTIFIER;
}
