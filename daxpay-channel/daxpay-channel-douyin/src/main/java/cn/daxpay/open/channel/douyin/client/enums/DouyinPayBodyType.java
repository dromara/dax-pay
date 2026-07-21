package cn.daxpay.open.channel.douyin.client.enums;

/// # 抖音支付内容类型
///
/// 与子应用 dax-pay-channel-one 的 `DouyinPayBodyType` 枚举 name 对齐。
public enum DouyinPayBodyType {
    /// 二维码链接(扫码支付)
    QR_CODE,
    /// 跳转链接(H5 支付)
    LINK,
    /// JSAPI 调起参数(JSON, 含 appId/timeStamp/nonceStr/package/signType/paySign)
    JSAPI,
    /// 标识符(APP 返回的 prepayId)
    IDENTIFIER
}
