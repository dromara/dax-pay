package cn.daxpay.open.channel.helipay.client.enums;

/// # 合利宝支付方式(主应用侧镜像)
///
/// 与子应用 `cn.daxpay.open.channel.helipay.enums.HelipayPayMethod` 枚举名对齐,
/// 经 JSON 传输由子应用直接反序列化; 取值对齐 pay_md_product_capability 主数据
/// (alipay_qr / alipay_jsapi / wechat_qr / wechat_jsapi / wechat_h5)。
public enum HelipayPayMethod {

    /// 支付宝扫码
    ALIPAY_QR,

    /// 微信扫码
    WECHAT_QR,

    /// 支付宝生活号/JSAPI
    ALIPAY_JSAPI,

    /// 微信公众号JSAPI
    WECHAT_JSAPI,

    /// 微信H5
    WECHAT_H5
}
