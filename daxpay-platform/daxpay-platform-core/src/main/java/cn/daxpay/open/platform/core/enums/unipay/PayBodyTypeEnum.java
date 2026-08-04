package cn.daxpay.open.platform.core.enums.unipay;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 支付参数体类型枚举
///
@Getter
@RequiredArgsConstructor
public enum PayBodyTypeEnum implements I18nSupport {

    /// 支付链接
    LINK("link"),
    /// JSAPI对象
    JSAPI("jsapi"),
    /// 表单数据
    FROM("from"),
    /// 标识码
    IDENTIFIER("identifier"),
    /// 二维码内容(前端渲染成二维码图片)
    QR_CODE("qr_code"),
    /// JSON对象
    JSON("json"),
    /// Stripe Checkout Session 跳转 URL(国际信用卡)
    STRIPE_CHECKOUT("stripe_checkout"),
    /// Stripe PaymentIntent client_secret(前端 Stripe.js Elements 调 confirmCardPayment)
    STRIPE_INTENT("stripe_intent");

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.pay_body_type";
    }

}
