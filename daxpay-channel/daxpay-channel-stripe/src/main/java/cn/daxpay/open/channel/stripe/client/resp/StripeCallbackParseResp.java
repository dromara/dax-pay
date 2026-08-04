package cn.daxpay.open.channel.stripe.client.resp;

import lombok.Data;

/// # Stripe Webhook 回调解析结果
@Data
public class StripeCallbackParseResp {

    /// 事件类型(payment_intent.succeeded / charge.refunded 等)
    private String eventType;

    /// 关联的平台业务单号(从 metadata.orderNo 取)
    private String orderNo;

    /// 关联的退款单号(退款回调时从 metadata.refundNo 取)
    private String refundNo;

    /// 支付/退款状态(succeeded / failed)
    private String tradeStatus;

    /// 完成时间(ISO8601 UTC)
    private String finishTime;

    /// 金额(最小货币单位)
    private Long amount;

    /// 币种
    private String currency;

    /// Stripe 对象 ID(pi_xxx / re_xxx)
    private String outOrderNo;
}
