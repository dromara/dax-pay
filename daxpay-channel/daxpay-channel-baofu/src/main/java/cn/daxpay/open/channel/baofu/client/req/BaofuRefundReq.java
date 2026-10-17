package cn.daxpay.open.channel.baofu.client.req;

import cn.daxpay.open.channel.baofu.client.credential.BaofuSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/// # 宝付通道退款请求(主应用侧)
///
/// 走 order_refund, 按宝付原交易号原路退回(缺省时子应用先按商户订单号查单补齐); 退款为同步终态。
@Data
public class BaofuRefundReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private BaofuSdkCredential credential;

    /// 商户退款单号(主应用退款关联号)
    @NotBlank(message = "{validation.field.outRefundNo.notBlank}")
    private String outRefundNo;

    /// 原商户订单号(原支付时上送的 outTradeNo, 即平台支付交易号)
    @NotBlank(message = "{validation.field.originOutTradeNo.notBlank}")
    private String originOutTradeNo;

    /// 原宝付交易号(originTradeNo, 可空; 为空时子应用先按原商户订单号查单获取)
    private String originOutOrderNo;

    /// 退款金额(单位: 分)
    @NotNull(message = "{validation.field.amount.notNull}")
    @Positive(message = "{validation.field.amount.positive}")
    private Long amount;

    /// 退款原因(refundReason)
    private String reason;
}
