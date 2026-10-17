package cn.daxpay.open.channel.yisheng.client.req;

import cn.daxpay.open.channel.yisheng.client.credential.YishengSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/// # 易生通道退款请求
///
/// 由主应用 dax-pay-open 经声明式 HTTP 客户端转发, 走 easypay.merchant.refund:
/// 按原商户订单号(out_trade_no = 原支付交易号)原路退回, 退款单号复用 out_trade_no 字段上送。
@Data
public class YishengRefundReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private YishengSdkCredential credential;

    /// 商户退款单号(主应用退款关联号, 作为本次退款的 out_trade_no 上送)
    @NotBlank(message = "{validation.field.outRefundNo.notBlank}")
    private String outRefundNo;

    /// 原商户订单号(原支付时上送的 out_trade_no = 原支付交易号, 作为 origin_trade_no 上送)
    @NotBlank(message = "{validation.field.originOutTradeNo.notBlank}")
    private String originOutTradeNo;

    /// 退款金额(单位: 分)
    @NotNull(message = "{validation.field.amount.notNull}")
    @Positive(message = "{validation.field.amount.positive}")
    private Long amount;
}
