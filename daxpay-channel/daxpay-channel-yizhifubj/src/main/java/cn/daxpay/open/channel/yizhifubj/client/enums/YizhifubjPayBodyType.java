package cn.daxpay.open.channel.yizhifubj.client.enums;

/// # 一直付支付内容类型(主应用侧)
///
/// 与子应用 YizhifubjPayBodyType 对齐, 用于映射平台 [cn.daxpay.open.platform.core.enums.unipay.PayBodyTypeEnum]。
public enum YizhifubjPayBodyType {
    /// 二维码内容(scanCodeUrl / scanCode)
    QR_CODE,
    /// 同步跳转页面(redirectUrl)
    REDIRECT,
    /// JsApi 调起串(jsString / tradeNo)
    JSAPI,
}
