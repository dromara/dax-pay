package cn.daxpay.open.channel.easypay.client.enums;

/// # 易支付支付内容类型(主应用侧)
///
/// 标识支付响应 pay_info 的内容形态, 由子应用按易支付响应 pay_type 动态判定后回传,
/// 主应用据此映射平台 [cn.daxpay.open.platform.core.enums.unipay.PayBodyTypeEnum]。
public enum EasyPayPayBodyType {
    /// 跳转链接(jump/qrcode)
    LINK,
    /// 二维码内容
    QR_CODE,
    /// JSAPI/小程序/APP 调起参数 JSON
    JSAPI,
    /// 表单数据(html)
    FROM,
    /// 通用标识码(app/urlscheme)
    IDENTIFIER,
    /// JSON 对象(未识别形态兜底)
    JSON;
}
