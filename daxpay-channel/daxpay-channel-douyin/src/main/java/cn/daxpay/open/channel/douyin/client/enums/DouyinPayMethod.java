package cn.daxpay.open.channel.douyin.client.enums;

/// # 抖音通道支付方式
///
/// 与子应用 dax-pay-channel-one 的 `DouyinPayMethod` 枚举 name 对齐,
/// 经 Jackson 序列化后按 name 传输, 子应用反序列化为对应枚举。
public enum DouyinPayMethod {
    /// 扫码支付(NATIVE)
    QR,
    /// 小程序/JSAPI 支付
    JSAPI,
    /// H5 支付
    H5,
    /// APP 支付
    APP
}
