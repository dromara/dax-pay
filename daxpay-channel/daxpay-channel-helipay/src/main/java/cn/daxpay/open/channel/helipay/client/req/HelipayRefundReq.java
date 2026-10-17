package cn.daxpay.open.channel.helipay.client.req;

import cn.daxpay.open.channel.helipay.client.credential.HelipaySdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/// # 合利宝通道退款请求(主应用 → 子应用)
///
/// 走 AppPayRefund 原路退回(P2_orderId=原商户订单号, P4_refundOrderId=退款单号)。
@Data
public class HelipayRefundReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private HelipaySdkCredential credential;

    /// 原商户订单号(原支付交易号)
    @NotBlank(message = "{validation.field.originOutTradeNo.notBlank}")
    private String originOutTradeNo;

    /// 商户退款单号(主应用退款交易号)
    @NotBlank(message = "{validation.field.outRefundNo.notBlank}")
    private String outRefundNo;

    /// 退款金额(单位: 分, 子应用换算为元上送)
    @NotNull(message = "{validation.field.amount.notNull}")
    @Positive(message = "{validation.field.amount.positive}")
    private Long amount;
}
