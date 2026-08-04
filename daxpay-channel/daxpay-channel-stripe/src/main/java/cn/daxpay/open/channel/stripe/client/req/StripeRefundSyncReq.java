package cn.daxpay.open.channel.stripe.client.req;

import cn.daxpay.open.channel.stripe.client.credential.StripeSdkCredential;
import lombok.Data;

/// # Stripe 退款同步请求参数
@Data
public class StripeRefundSyncReq {

    /// 通道凭证
    private StripeSdkCredential credential;

    /// Stripe Refund ID(re_xxx)
    private String refundId;
}
