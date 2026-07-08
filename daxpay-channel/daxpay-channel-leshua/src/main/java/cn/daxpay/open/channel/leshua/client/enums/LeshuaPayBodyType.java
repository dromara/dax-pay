package cn.daxpay.open.channel.leshua.client.enums;

/// # 乐刷支付内容类型(主应用侧镜像)
///
/// 与子应用 dax-pay-channel-two 的 `LeshuaPayBodyType` 镜像, 跨 HTTP 传输时按枚举名(name)序列化对齐。
public enum LeshuaPayBodyType {
    /// 二维码内容(扫码返回的 td_code / jspay_url)
    QR_CODE,
    /// JSAPI/小程序调起参数 JSON(jspay_info)
    JSAPI,
    /// 通用标识码(支付宝 JSAPI 的 trade_no)
    IDENTIFIER,
    /// 跳转链接(H5 / 云闪付 JSAPI 的跳转地址)
    LINK;
}
