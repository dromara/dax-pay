package cn.daxpay.open.channel.stripe.client.req;

import cn.daxpay.open.channel.stripe.client.credential.StripeSdkCredential;
import lombok.Data;

/// # Stripe 退款请求参数
@Data
public class StripeRefundReq {

    /// 通道凭证
    private StripeSdkCredential credential;

    /// Stripe PaymentIntent ID(关联原支付)
    private String paymentIntentId;

    /// 退款金额(最小货币单位)
    private Long amount;

    /// 退款原因(duplicate / fraudulent / requested_by_customer)
    private String reason;
}
