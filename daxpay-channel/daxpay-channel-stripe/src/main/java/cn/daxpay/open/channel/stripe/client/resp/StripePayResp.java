package cn.daxpay.open.channel.stripe.client.resp;

import lombok.Data;

/// # Stripe 支付响应
@Data
public class StripePayResp {

    /// Stripe 对象 ID(PaymentIntent pi_xxx / Checkout Session cs_test_xxx)
    private String outOrderNo;

    /// 支付参数体(Checkout URL 或 client_secret)
    private String payBody;

    /// 支付参数体类型(stripe_checkout / stripe_intent)
    private String payBodyType;

    /// 三方支付状态(succeeded / requires_payment_method / requires_action 等)
    private String tradeStatus;
}
