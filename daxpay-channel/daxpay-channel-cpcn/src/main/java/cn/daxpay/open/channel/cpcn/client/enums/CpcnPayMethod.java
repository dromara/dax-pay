package cn.daxpay.open.channel.cpcn.client.enums;

import lombok.Getter;

/// # 中金支付方式(主应用侧)
///
/// 中金 5011 统一下单通过 PaymentWay(支付大类) + PayType/ScanPaymentType(支付渠道) + PayWay/ScanPaymentWay(支付形态)
/// 三级码定位具体支付方式, 本枚举集中管理平台支付方式到中金三级码的映射。
@Getter
public enum CpcnPayMethod {

    /// 微信扫码(PaymentWay=42, ScanPaymentType=10, ScanPaymentWay=41)
    WECHAT_QR(CpcnPayWayKind.SCAN, "10", "41", false, false, CpcnPayBodyType.QR_CODE),
    /// 支付宝扫码(PaymentWay=42, ScanPaymentType=20, ScanPaymentWay=41)
    ALIPAY_QR(CpcnPayWayKind.SCAN, "20", "41", false, false, CpcnPayBodyType.QR_CODE),
    /// 云闪付扫码(PaymentWay=42, ScanPaymentType=30, ScanPaymentWay=41)
    UNION_QR(CpcnPayWayKind.SCAN, "30", "41", false, false, CpcnPayBodyType.QR_CODE),
    /// 微信公众号 JSAPI(PaymentWay=80, PayType=31, PayWay=50, 需 SubOpenID + SubAppID)
    WECHAT_JSAPI(CpcnPayWayKind.REDIRECT, "31", "50", true, true, CpcnPayBodyType.JSAPI),
    /// 微信 H5(PaymentWay=80, PayType=31, PayWay=45)
    WECHAT_H5(CpcnPayWayKind.REDIRECT, "31", "45", false, false, CpcnPayBodyType.LINK),
    /// 支付宝生活号/钱包 JSAPI(PaymentWay=80, PayType=32, PayWay=50, 需买家用户ID)
    ALIPAY_JSAPI(CpcnPayWayKind.REDIRECT, "32", "50", true, false, CpcnPayBodyType.JSAPI);

    /// 支付大类(42 被扫 / 80 跳转)
    private final CpcnPayWayKind kind;

    /// 支付渠道码(PaymentWay=42 时为 ScanPaymentType, PaymentWay=80 时为 PayType)
    private final String payType;

    /// 支付形态码(PaymentWay=42 时为 ScanPaymentWay, PaymentWay=80 时为 PayWay)
    private final String payWay;

    /// 是否必填 openId(微信=openid / 支付宝=买家用户ID)
    private final boolean needOpenId;

    /// 是否必填 channelAppId(微信公众号/小程序 AppId, 上送 SubAppID)
    private final boolean needAppId;

    /// 支付内容类型
    private final CpcnPayBodyType bodyType;

    CpcnPayMethod(CpcnPayWayKind kind, String payType, String payWay,
                  boolean needOpenId, boolean needAppId, CpcnPayBodyType bodyType) {
        this.kind = kind;
        this.payType = payType;
        this.payWay = payWay;
        this.needOpenId = needOpenId;
        this.needAppId = needAppId;
        this.bodyType = bodyType;
    }

    /// 支付大类枚举
    public enum CpcnPayWayKind {
        /// 被扫/扫码支付(PaymentWay=42)
        SCAN,
        /// 跳转支付(PaymentWay=80)
        REDIRECT
    }
}
