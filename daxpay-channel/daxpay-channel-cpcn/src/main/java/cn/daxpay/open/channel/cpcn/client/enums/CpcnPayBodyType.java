package cn.daxpay.open.channel.cpcn.client.enums;

/// # 中金支付内容类型(主应用侧)
///
/// 标识 `CpcnPayResp.payBody` 的内容形态, 主应用据此决定如何渲染支付页。
public enum CpcnPayBodyType {
    /// 二维码内容(PaymentWay=42 被扫下单返回的 QRCodeURL)
    QR_CODE,
    /// JSAPI 调起参数(微信 jsApiParameters JSON / 支付宝 trade_no 交易串)
    JSAPI,
    /// 跳转链接(PaymentWay=80 H5 下单返回的 QRAuthCode 跳转地址)
    LINK
}
