package cn.daxpay.open.channel.easypay.client.req;

import cn.daxpay.open.channel.easypay.client.credential.EasyPaySdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/// # 易支付通道退款请求
///
/// 由主应用 dax-pay-open 经声明式 HTTP 客户端转发。
/// 原单定位: 通道交易号(originTradeNo)优先, 为空时用商户订单号(originOutTradeNo)。
///
/// 金额单位为「分」(平台内部单位), 子应用换算为易支付「元」后上送。
@Data
public class EasyPayRefundReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private EasyPaySdkCredential credential;

    /// 商户退款单号(主应用退款交易号, 作为易支付 out_refund_no)
    @NotBlank(message = "{validation.field.outRefundNo.notBlank}")
    private String outRefundNo;

    /// 原商户订单号(原支付 tradeNo)
    private String originOutTradeNo;

    /// 原易支付交易号(优先使用)
    private String originTradeNo;

    /// 退款金额(单位: 分)
    @NotNull(message = "{validation.field.amount.notNull}")
    @Positive(message = "{validation.field.amount.positive}")
    private Long amount;
}
