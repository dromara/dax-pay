package cn.daxpay.open.channel.fubei.client.enums;

/// # 付呗支付内容类型(主应用侧)
///
/// 标识 `FubeiPayResp.payBody` 的内容形态, 主应用据此决定如何渲染支付页。
public enum FubeiPayBodyType {
    /// 二维码内容(云闪付扫码下单返回的 qr_code)
    QR_CODE,
    /// JSAPI 调起参数(微信返回 sign_package JSON / 支付宝返回 prepay_id 交易串)
    JSAPI,
    /// 自提交表单 HTML(支付宝 H5 下单返回的 html, http 开头的跳转 URL 场景用 LINK)
    FROM,
    /// 跳转链接(支付宝 H5 下单返回 http 开头的跳转地址)
    LINK
}
