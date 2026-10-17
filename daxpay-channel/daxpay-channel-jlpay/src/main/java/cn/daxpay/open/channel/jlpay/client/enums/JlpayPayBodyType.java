package cn.daxpay.open.channel.jlpay.client.enums;

/// # 嘉联支付内容类型
///
/// 标识 `JlpayPayResp.payBody` 的内容形态, 主应用据此决定如何渲染支付页。
public enum JlpayPayBodyType {
    /// 二维码内容(qrcodepay 下单返回的 code_url)
    QR_CODE;
}
