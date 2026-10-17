package cn.daxpay.open.channel.llianpay.client.enums;

/// # 连连支付内容类型(主应用侧)
///
/// 与子应用 LlianpayPayBodyType 对齐, 用于映射平台 [cn.daxpay.open.platform.core.enums.unipay.PayBodyTypeEnum]。
public enum LlianpayPayBodyType {
    /// 二维码内容(统收单扫码族 payload)
    QR_CODE,
    /// JSAPI 调起参数(支付宝交易号串 / 微信 jsapi JSON 串)
    JSAPI,
    /// 跳转链接(收银台 gateway_url)
    LINK;
}
