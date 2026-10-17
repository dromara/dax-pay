package cn.daxpay.open.channel.epaylinks.client.req;

import cn.daxpay.open.channel.epaylinks.client.credential.EpaylinksSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/// # 易票联通道退款请求(主应用侧)
@Data
public class EpaylinksRefundReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private EpaylinksSdkCredential credential;

    /// 商户退款单号(主应用退款关联号, 作为 outRefundNo 上送)
    @NotBlank(message = "{validation.field.outRefundNo.notBlank}")
    private String outRefundNo;

    /// 易票联平台交易号(transactionNo, 来自支付回调/查单)
    @NotBlank(message = "{validation.field.tradeNo.notBlank}")
    private String outTransNo;

    /// 退款金额(单位: 分)
    @NotNull(message = "{validation.field.amount.notNull}")
    @Positive(message = "{validation.field.amount.positive}")
    private Long amount;
}
