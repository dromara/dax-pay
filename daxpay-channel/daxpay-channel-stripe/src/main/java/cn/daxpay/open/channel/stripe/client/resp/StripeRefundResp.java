package cn.daxpay.open.channel.stripe.client.resp;

import lombok.Data;

/// # Stripe 退款响应
@Data
public class StripeRefundResp {

    /// Stripe Refund ID(re_xxx)
    private String outRefundNo;

    /// 退款状态(succeeded / processing / failed)
    private String tradeStatus;
}
