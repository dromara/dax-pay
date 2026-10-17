package cn.daxpay.open.channel.xingyi.client.enums;

/// # 星驿付支付内容类型(主应用侧)
///
/// 与子应用 XingyiPayBodyType 对齐, 用于映射平台 [cn.daxpay.open.platform.core.enums.unipay.PayBodyTypeEnum]。
public enum XingyiPayBodyType {
    /// 二维码内容(官方码/星驿码链接)
    QR_CODE,
    /// JSAPI/小程序调起参数(支付宝为 prepayid 交易串, 微信为调起参数 JSON)
    JSAPI;
}
