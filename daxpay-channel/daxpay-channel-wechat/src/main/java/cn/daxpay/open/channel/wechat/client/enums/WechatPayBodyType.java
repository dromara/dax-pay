package cn.daxpay.open.channel.wechat.client.enums;

/// # 微信支付内容类型
///
/// 与子应用 dax-pay-channel-one 的 `WechatPayBodyType` 镜像, 跨 HTTP 传输时按枚举名(name)序列化对齐。
public enum WechatPayBodyType {
    /// 跳转链接(H5 场景)
    LINK,
    /// 二维码内容(NATIVE 扫码场景)
    QR_CODE,
    /// 调起参数 JSON(JSAPI / 小程序场景)
    IDENTIFIER,
    /// APP 调起参数 JSON(APP 场景)
    APP_ORDER_STR;
}
