package cn.daxpay.open.channel.longpay.client.enums;

/// # 建行龙支付支付内容类型(主应用侧)
///
/// 与子应用 [LongpayPayBodyType] 对齐, 标识 `LongpayPayResp.payBody` 的内容形态。
public enum LongpayPayBodyType {
    /// 二维码内容(聚合下单返回的 QRCODE 码串)
    QR_CODE;
}
