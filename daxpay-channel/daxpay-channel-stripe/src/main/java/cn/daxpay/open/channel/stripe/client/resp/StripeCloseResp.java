package cn.daxpay.open.channel.stripe.client.resp;

import lombok.Data;

/// # Stripe 关单响应
@Data
public class StripeCloseResp {

    /// PaymentIntent 最终状态(canceled)
    private String tradeStatus;
}
