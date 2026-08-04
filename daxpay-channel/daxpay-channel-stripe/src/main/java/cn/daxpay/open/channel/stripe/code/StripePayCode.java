package cn.daxpay.open.channel.stripe.code;

/// # Stripe 支付常量
///
/// Stripe 通道的支付方式 / 状态 / 事件类型 / 回调应答常量。
public interface StripePayCode {

    /// 支付方式: Checkout Session(Stripe 托管收银台, 跳转模式)
    String PAY_METHOD_CHECKOUT = "stripe_checkout";

    /// 支付方式: PaymentIntent + Elements(自嵌卡组件, API 模式)
    String PAY_METHOD_INTENT = "stripe_payment_intent";

    /// PaymentIntent 状态: 成功
    String INTENT_STATUS_SUCCEEDED = "succeeded";

    /// PaymentIntent 状态: 需支付方式(未支付)
    String INTENT_STATUS_REQUIRES_PAYMENT_METHOD = "requires_payment_method";

    /// PaymentIntent 状态: 需客户确认(3DS 挑战等)
    String INTENT_STATUS_REQUIRES_CONFIRMATION = "requires_confirmation";

    /// PaymentIntent 状态: 已取消
    String INTENT_STATUS_CANCELED = "canceled";

    /// PaymentIntent 状态: 处理中(异步扣款)
    String INTENT_STATUS_PROCESSING = "processing";

    /// Refund 状态: 成功
    String REFUND_STATUS_SUCCEEDED = "succeeded";

    /// Refund 状态: 处理中
    String REFUND_STATUS_PROCESSING = "processing";

    /// Refund 状态: 失败
    String REFUND_STATUS_FAILED = "failed";

    /// Webhook 事件: 支付成功
    String EVENT_PAYMENT_INTENT_SUCCEEDED = "payment_intent.succeeded";

    /// Webhook 事件: 支付失败
    String EVENT_PAYMENT_INTENT_PAYMENT_FAILED = "payment_intent.payment_failed";

    /// Webhook 事件: 退款成功
    String EVENT_CHARGE_REFUNDED = "charge.refunded";

    /// Webhook 事件: 退款更新
    String EVENT_REFUND_UPDATED = "refund.updated";

    /// Webhook 签名头
    String HEADER_SIGNATURE = "Stripe-Signature";

    /// 回调成功应答(Stripe 要求 2xx 即视为成功, 返回空 JSON)
    String NOTIFY_SUCCESS = "{}";

    /// 回调失败应答
    String NOTIFY_FAIL = "{}";
}
