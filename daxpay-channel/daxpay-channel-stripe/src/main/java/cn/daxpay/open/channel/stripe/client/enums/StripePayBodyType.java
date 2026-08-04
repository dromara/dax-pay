package cn.daxpay.open.channel.stripe.client.enums;

/// # Stripe 支付体类型(子应用返回, 与平台 PayBodyTypeEnum 映射)
public enum StripePayBodyType {

    /// Checkout Session 跳转 URL(前端 window.location 跳转到 Stripe 托管页)
    CHECKOUT_URL("stripe_checkout"),

    /// PaymentIntent client_secret(前端用 Stripe.js Elements 调 confirmCardPayment)
    INTENT_SECRET("stripe_intent"),
    ;

    private final String code;

    StripePayBodyType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
