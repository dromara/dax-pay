package cn.daxpay.open.channel.stripe.client.req;

import cn.daxpay.open.channel.stripe.client.credential.StripeSdkCredential;
import lombok.Data;

/// # Stripe 关单请求参数(取消 PaymentIntent)
@Data
public class StripeCloseReq {

    /// 通道凭证
    private StripeSdkCredential credential;

    /// Stripe PaymentIntent ID(pi_xxx)
    private String paymentIntentId;

    /// 平台业务单号
    private String orderNo;
}
