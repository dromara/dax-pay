package cn.daxpay.open.channel.stripe.client.req;

import cn.daxpay.open.channel.stripe.client.credential.StripeSdkCredential;
import lombok.Data;

/// # Stripe 支付同步请求参数
@Data
public class StripeSyncReq {

    /// 通道凭证
    private StripeSdkCredential credential;

    /// Stripe PaymentIntent ID
    private String paymentIntentId;
}
