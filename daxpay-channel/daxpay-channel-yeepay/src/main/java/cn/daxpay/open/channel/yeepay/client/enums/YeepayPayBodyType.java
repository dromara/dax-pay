package cn.daxpay.open.channel.yeepay.client.enums;

/// # 易宝支付内容类型(主应用侧)
///
/// 与子应用 cn.daxpay.open.channel.yeepay.enums.YeepayPayBodyType 对齐。
public enum YeepayPayBodyType {

    /// 二维码链接(扫码支付)
    QR_CODE,

    /// 跳转链接(H5 支付)
    LINK,
}
