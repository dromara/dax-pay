package cn.daxpay.open.channel.vbill.client.req;

import cn.daxpay.open.channel.vbill.client.credential.VbillSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class VbillRefundReq {

    @NotNull(message = "{validation.field.credential.notNull}")
    private VbillSdkCredential credential;

    /// 商户退款单号(主应用退款交易号)
    @NotBlank(message = "{validation.field.outRefundNo.notBlank}")
    private String outRefundNo;

    /// 原支付订单的网关订单号(随行付 uuid)
    @NotBlank(message = "{validation.field.outOrderNo.notBlank}")
    private String outOrderNo;

    /// 退款金额(单位: 分)
    @NotNull(message = "{validation.field.amount.notNull}")
    @Positive(message = "{validation.field.amount.positive}")
    private Long amount;

    /// 退款原因
    private String reason;

    /// 退款异步通知地址(由子应用透传给随行付)
    private String notifyUrl;
}
