package cn.daxpay.open.channel.helipay.client.enums;

/// # 合利宝支付内容类型(主应用侧镜像)
///
/// 与子应用 `cn.daxpay.open.channel.helipay.enums.HelipayPayBodyType` 对齐,
/// 由主应用映射为平台 [cn.daxpay.open.platform.core.enums.unipay.PayBodyTypeEnum]。
public enum HelipayPayBodyType {

    /// 二维码内容(扫码支付)
    QR_CODE,

    /// JSAPI 调起参数(公众号/生活号)
    JSAPI,

    /// 跳转链接(H5 支付)
    LINK
}
