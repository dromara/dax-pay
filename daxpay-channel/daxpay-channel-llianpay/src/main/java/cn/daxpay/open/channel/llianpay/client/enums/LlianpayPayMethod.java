package cn.daxpay.open.channel.llianpay.client.enums;

/// # 连连支付方式(主应用侧)
///
/// 与子应用 LlianpayPayMethod 对齐(含接口形态 Kind), 用于主应用白名单映射与子应用请求组装。
public enum LlianpayPayMethod {

    /// 微信扫码(WECHAT_NATIVE, 二维码)
    WECHAT_NATIVE(Kind.QR),
    /// 支付宝扫码(ALIPAY_NATIVE, 二维码)
    ALIPAY_NATIVE(Kind.QR),
    /// 云闪付扫码(CLOUDPAY_APP, 二维码)
    CLOUDPAY_APP(Kind.QR),
    /// 微信公众号支付(WECHAT_JSAPI, 需 appid+openid)
    WECHAT_JSAPI(Kind.JSAPI_WECHAT),
    /// 微信小程序支付(WECHAT_APPLET, 需 appid+openid)
    WECHAT_APPLET(Kind.JSAPI_WECHAT),
    /// 支付宝 JS/小程序支付(ALIPAY_APPLET, 需支付宝买家ID)
    ALIPAY_APPLET(Kind.JSAPI_ALIPAY),
    /// 连连收银台(CASHIER, 跳转链接)
    CASHIER(Kind.CASHIER);

    /// 接口形态(决定请求组装与响应解析方式)
    private final Kind kind;

    LlianpayPayMethod(Kind kind) {
        this.kind = kind;
    }

    public Kind getKind() {
        return kind;
    }

    /// 连连支付方式的接口形态
    public enum Kind {
        /// 扫码下单(createpay, payload 为二维码链接)
        QR,
        /// 微信 JSAPI 族下单(createpay + extend_info.wx_data, payload 为 jsapi 调起串)
        JSAPI_WECHAT,
        /// 支付宝 JSAPI 族下单(createpay + extend_info.alipay_data, payload 为支付宝交易号)
        JSAPI_ALIPAY,
        /// 收银台下单(cashier/createpay, 响应 gateway_url 跳转链接)
        CASHIER
    }
}
