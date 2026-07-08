package cn.daxpay.open.channel.hmpay.client.enums;

/// # 河马付支付内容类型(主应用侧)
///
/// 与子应用 dax-pay-channel-two 的 `HmpayPayBodyType` 镜像, 跨 HTTP 传输时按枚举名(name)序列化对齐。
public enum HmpayPayBodyType {
    /// 二维码内容(扫码支付 qr_code)
    QR_CODE,
    /// JSAPI/小程序调起参数(微信 pay_data JSON)
    JSAPI,
    /// 通用标识码(支付宝 JSAPI prepay_id)
    IDENTIFIER;
}
