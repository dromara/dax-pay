package cn.daxpay.open.channel.alipay.client.enums;

/// # 支付内容类型
///
/// 与子应用 dax-pay-channel-one 的 `AlipayPayBodyType` 镜像, 跨 HTTP 传输时按枚举名(name)序列化对齐。
public enum AlipayPayBodyType {
    /// 跳转链接(wap / pc 场景, 前端可直接 location.href)
    LINK,
    /// 二维码内容(qr 扫码场景)
    QR_CODE,
    /// APP 订单串(app 场景)
    ORDER_STR,
    /// 标识符(jsapi 场景, 支付宝交易号 tradeNo)
    IDENTIFIER;
}
