package cn.daxpay.open.channel.kuaiqian.client.req;

import cn.daxpay.open.channel.kuaiqian.client.credential.KuaiqianSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/// # 快钱通道退款请求(主应用侧)
///
/// 走 MAS 网关 A7003 当面付退款, 退款结果经 tr3Url 异步通知。
@Data
public class KuaiqianRefundReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private KuaiqianSdkCredential credential;

    /// 商户退款单号(主应用退款关联号)
    @NotBlank(message = "{validation.field.outRefundNo.notBlank}")
    private String outRefundNo;

    /// 原商户订单号(原支付交易号)
    @NotBlank(message = "{validation.field.originOutTradeNo.notBlank}")
    private String originOutTradeNo;

    /// 原快钱订单控制号(origOrderCtrl)
    @NotBlank(message = "{validation.field.tradeNo.notBlank}")
    private String originOutOrderNo;

    /// 退款金额(单位: 分)
    @NotNull(message = "{validation.field.amount.notNull}")
    @Positive(message = "{validation.field.amount.positive}")
    private Long amount;

    /// 退款原因
    private String reason;

    /// 异步通知地址(tr3Url)
    private String notifyUrl;
}
