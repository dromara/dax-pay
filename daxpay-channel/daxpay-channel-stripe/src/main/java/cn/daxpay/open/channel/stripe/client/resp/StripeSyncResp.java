package cn.daxpay.open.channel.stripe.client.resp;

import lombok.Data;

/// # Stripe 支付同步响应
@Data
public class StripeSyncResp {

    /// PaymentIntent ID(pi_xxx)
    private String outOrderNo;

    /// PaymentIntent 状态(succeeded / requires_payment_method / canceled 等)
    private String tradeStatus;

    /// 支付时间(ISO8601 UTC, 成功时才有)
    private String payTime;

    /// 实际收单金额(最小货币单位)
    private Long amount;

    /// 币种
    private String currency;
}
