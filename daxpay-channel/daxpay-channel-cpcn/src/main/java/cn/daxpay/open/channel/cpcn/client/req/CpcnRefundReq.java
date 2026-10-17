package cn.daxpay.open.channel.cpcn.client.req;

import cn.daxpay.open.channel.cpcn.client.credential.CpcnSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/// # 中金通道退款请求(主应用侧)
///
/// 走中金 5021 退款, 按原支付流水号 PaymentTxSN 原路退回, 退款结果经 NoticeURL 异步通知。
@Data
public class CpcnRefundReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private CpcnSdkCredential credential;

    /// 商户退款单号(主应用退款关联号, 作为退款 TxSN 上送)
    @NotBlank(message = "{validation.field.outRefundNo.notBlank}")
    private String outRefundNo;

    /// 原商户订单号(原支付下单时上送的 TxSN, 即主平台支付交易号, 上送 PaymentTxSN)
    @NotBlank(message = "{validation.field.originOutTradeNo.notBlank}")
    private String originOutTradeNo;

    /// 退款金额(单位: 分)
    @NotNull(message = "{validation.field.amount.notNull}")
    @Positive(message = "{validation.field.amount.positive}")
    private Long amount;

    /// 退款原因
    private String reason;

    /// 异步通知地址(NoticeURL, 由子应用透传给中金)
    private String notifyUrl;
}
