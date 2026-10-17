package cn.daxpay.open.channel.fubei.client.req;

import cn.daxpay.open.channel.fubei.client.credential.FubeiSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/// # 付呗通道退款请求(主应用侧)
///
/// 走 fbpay.order.refund, 按付呗订单号(order_sn)原路退回, 同步返回受理结果。
@Data
public class FubeiRefundReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private FubeiSdkCredential credential;

    /// 商户退款单号(主应用退款关联号, 作为 merchant_refund_sn 上送)
    @NotBlank(message = "{validation.field.outRefundNo.notBlank}")
    private String outRefundNo;

    /// 原商户订单号(原支付下单时的 merchant_order_sn, 即主平台支付交易号)
    @NotBlank(message = "{validation.field.originOutTradeNo.notBlank}")
    private String originOutTradeNo;

    /// 原付呗订单号(order_sn, 下单响应/回调获得)
    @NotBlank(message = "{validation.field.tradeNo.notBlank}")
    private String originOutOrderNo;

    /// 退款金额(单位: 分, 付呗接口金额单位为「元」, 子应用自动换算)
    @NotNull(message = "{validation.field.amount.notNull}")
    @Positive(message = "{validation.field.amount.positive}")
    private Long amount;

    /// 退款原因
    private String reason;
}
