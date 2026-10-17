package cn.daxpay.open.channel.umfpay.client.enums;

/// # 联动优势支付内容类型(主应用侧)
///
/// 与子应用 cn.daxpay.open.channel.umfpay.enums.UmfpayPayBodyType 枚举项保持一致(序列化互通)。
public enum UmfpayPayBodyType {
    /// 二维码内容(扫码下单返回的 bank_payurl 链接)
    QR_CODE,
    /// 跳转链接(公众号支付组装的 GET 表单跳转 URL)
    LINK;
}
