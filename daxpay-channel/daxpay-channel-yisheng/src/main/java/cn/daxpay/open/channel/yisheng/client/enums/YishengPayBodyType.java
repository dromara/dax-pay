package cn.daxpay.open.channel.yisheng.client.enums;

/// # 易生支付内容类型
///
/// 标识 `YishengPayResp.payBody` 的内容形态, 主应用据此决定如何渲染支付页。
public enum YishengPayBodyType {
    /// 二维码内容(扫码下单返回的 code_url)
    QR_CODE,
    /// 微信 JSAPI 调起参数(下单返回的 pay_info JSON 原文)
    JSAPI,
    /// 支付宝交易号标识(支付宝 JS 下单返回的 alipayTradeNo)
    IDENTIFIER
}
