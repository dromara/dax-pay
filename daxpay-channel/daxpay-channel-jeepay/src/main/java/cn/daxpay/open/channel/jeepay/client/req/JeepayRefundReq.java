package cn.daxpay.open.channel.jeepay.client.req;

import cn.daxpay.open.channel.jeepay.client.credential.JeepaySdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/// # Jeepay通道退款请求(主应用侧)
///
/// 原单定位: 通道交易号(originTradeNo, 即 Jeepay payOrderId)优先, 为空时用商户订单号(originOutTradeNo)。
///
/// Jeepay 金额单位为「分」(与平台内部单位一致, 直传不换算)。
@Data
public class JeepayRefundReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private JeepaySdkCredential credential;

    /// 商户退款单号(主应用退款交易号, 作为 Jeepay mchRefundNo)
    @NotBlank(message = "{validation.field.outRefundNo.notBlank}")
    private String outRefundNo;

    /// 原商户订单号(原支付 tradeNo)
    private String originOutTradeNo;

    /// 原Jeepay支付订单号(payOrderId, 优先使用)
    private String originTradeNo;

    /// 退款金额(单位: 分)
    @NotNull(message = "{validation.field.amount.notNull}")
    @Positive(message = "{validation.field.amount.positive}")
    private Long amount;
}
