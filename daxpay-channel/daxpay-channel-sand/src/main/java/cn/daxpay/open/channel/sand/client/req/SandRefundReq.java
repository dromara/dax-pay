package cn.daxpay.open.channel.sand.client.req;

import cn.daxpay.open.channel.sand.client.credential.SandSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/// # 杉德通道退款请求(主应用 → 子应用)
///
/// 金额单位为「分」(平台内部单位), 子应用侧换算为杉德要求的「元」。
@Data
public class SandRefundReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private SandSdkCredential credential;

    /// 商户退款单号(主应用退款关联号, 作为本次退款订单的 outOrderNo 上送)
    @NotBlank(message = "{validation.field.outRefundNo.notBlank}")
    private String outRefundNo;

    /// 原商户订单号(原支付下单时的 outOrderNo = 主平台支付交易号)
    @NotBlank(message = "{validation.field.originOutTradeNo.notBlank}")
    private String originOutTradeNo;

    /// 退款金额(单位: 分)
    @NotNull(message = "{validation.field.amount.notNull}")
    @Positive(message = "{validation.field.amount.positive}")
    private Long amount;

    /// 退款异步通知地址(notifyUrl)
    private String notifyUrl;
}
