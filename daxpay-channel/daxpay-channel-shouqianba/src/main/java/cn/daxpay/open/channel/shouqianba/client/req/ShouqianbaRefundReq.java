package cn.daxpay.open.channel.shouqianba.client.req;

import cn.daxpay.open.channel.shouqianba.client.credential.ShouqianbaSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/// # 收钱吧通道退款请求(主应用侧)
///
/// 走 upay/v2/refund 按原支付流水号 sn 原路退回, 退款为同步终态(REFUND_SUCCESS 即成功)。
@Data
public class ShouqianbaRefundReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private ShouqianbaSdkCredential credential;

    /// 商户退款单号(作为 refund_request_no 上送, 收钱吧侧幂等键)
    @NotBlank(message = "{validation.field.outRefundNo.notBlank}")
    private String outRefundNo;

    /// 原支付收钱吧流水号(sn, 支付回调/查单获得)
    @NotBlank(message = "{validation.field.tradeNo.notBlank}")
    private String originOutOrderNo;

    /// 退款金额(单位: 分)
    @NotNull(message = "{validation.field.amount.notNull}")
    @Positive(message = "{validation.field.amount.positive}")
    private Long amount;

    /// 退款原因(可选)
    private String reason;
}
