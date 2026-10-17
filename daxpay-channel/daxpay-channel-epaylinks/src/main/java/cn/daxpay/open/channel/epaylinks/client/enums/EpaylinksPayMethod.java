package cn.daxpay.open.channel.epaylinks.client.enums;

/// # 易票联支付方式(主应用侧)
///
/// 与子应用 EpaylinksPayMethod 对齐(含接口形态 Kind), 用于主应用白名单映射与子应用请求组装。
public enum EpaylinksPayMethod {

    /// 微信扫码(payMethod=6, 二维码)
    WECHAT_NATIVE(Kind.QR),
    /// 支付宝扫码(payMethod=7, 二维码)
    ALIPAY_NATIVE(Kind.QR),
    /// 云闪付扫码(payMethod=24, 二维码)
    UNION_NATIVE(Kind.QR),
    /// 微信公众号支付(payMethod=1, 需 appId+openid)
    WECHAT_JSAPI(Kind.JSAPI_WECHAT),
    /// 微信小程序支付(payMethod=35, 需 appId+openid)
    WECHAT_APPLET(Kind.JSAPI_WECHAT),
    /// 支付宝生活号/小程序支付(需支付宝买家ID buyerId)
    ALIPAY_JSAPI(Kind.JSAPI_ALIPAY),
    /// 个人网银(统一收银台 payMethod=3, 跳转链接)
    WEB_BANK_CASHIER(Kind.CASHIER);

    /// 接口形态(决定请求组装与响应解析方式)
    private final Kind kind;

    EpaylinksPayMethod(Kind kind) {
        this.kind = kind;
    }

    public Kind getKind() {
        return kind;
    }

    /// 易票联支付方式的接口形态
    public enum Kind {
        /// 主扫下单(NativePayment, payload 为二维码链接)
        QR,
        /// 微信 JSAPI 族下单(WxJSAPIPayment, payload 为调起参数)
        JSAPI_WECHAT,
        /// 支付宝 JSAPI 族下单(AliJSAPIPayment, payload 为支付宝交易号)
        JSAPI_ALIPAY,
        /// 统一收银台下单(UnifiedPayment, 响应 casherUrl 跳转链接)
        CASHIER
    }
}
