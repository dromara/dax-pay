package cn.daxpay.open.channel.stripe.client.resp;

import lombok.Data;

/// # Stripe 退款同步响应
@Data
public class StripeRefundSyncResp {

    /// Stripe Refund ID(re_xxx)
    private String outRefundNo;

    /// 退款状态(succeeded / processing / failed)
    private String tradeStatus;

    /// 退款完成时间(ISO8601 UTC)
    private String finishTime;

    /// 退款金额(最小货币单位)
    private Long amount;
}
