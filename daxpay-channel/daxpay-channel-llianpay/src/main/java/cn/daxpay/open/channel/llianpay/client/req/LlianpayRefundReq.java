package cn.daxpay.open.channel.llianpay.client.req;

import cn.daxpay.open.channel.llianpay.client.credential.LlianpaySdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/// # 连连通道退款请求(主应用侧)
///
/// 走连连统收单退款, 按原商户单号原路退回。
/// 连连要求上送原支付 pay_type 与原单金额, 子应用先反查原单后发起。
@Data
public class LlianpayRefundReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private LlianpaySdkCredential credential;

    /// 商户退款单号(作为 refund_seqno 上送)
    @NotBlank(message = "{validation.field.outRefundNo.notBlank}")
    private String outRefundNo;

    /// 原商户订单号(原支付的 txn_seqno = 平台支付交易号)
    @NotBlank(message = "{validation.field.originOutTradeNo.notBlank}")
    private String originOutTradeNo;

    /// 退款金额(单位: 分)
    @NotNull(message = "{validation.field.amount.notNull}")
    @Positive(message = "{validation.field.amount.positive}")
    private Long amount;

    /// 原订单金额(单位: 分; 连连退款要求上送原单金额)
    @NotNull(message = "{validation.field.orderAmount.notNull}")
    @Positive(message = "{validation.field.orderAmount.positive}")
    private Long originAmount;

    /// 异步通知地址
    private String notifyUrl;
}
