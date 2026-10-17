package cn.daxpay.open.channel.sand.client.enums;

/// # 杉德支付内容类型(主应用侧)
///
/// 与子应用 SandPayBodyType 对齐, 用于映射平台 [cn.daxpay.open.platform.core.enums.unipay.PayBodyTypeEnum]。
public enum SandPayBodyType {
    /// 二维码内容(credential.qrCode)
    QR_CODE,
    /// JSAPI 调起参数 JSON(微信 credential 原文)
    JSAPI,
    /// 支付交易号标识(支付宝 credential.tradeNo)
    IDENTIFIER
}
