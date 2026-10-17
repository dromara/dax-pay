package cn.daxpay.open.channel.youzan.client.req;

import cn.daxpay.open.channel.youzan.client.credential.YouzanSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/// # 有赞通道退款请求
///
/// 由主应用 dax-pay-open 经声明式 HTTP 客户端转发, 走有赞开放网关统一退款
/// (youzan.pay.unified.refundopen.apply/1.0.0), 按原渠道订单号(trade_no)原路退回。
@Data
public class YouzanRefundReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private YouzanSdkCredential credential;

    /// 商户退款单号(主应用退款关联号, 作为 out_refund_no 上送)
    @NotBlank(message = "{validation.field.outRefundNo.notBlank}")
    private String outRefundNo;

    /// 原有赞订单号(原支付下单返回的 trade_no)
    @NotBlank(message = "{validation.field.tradeNo.notBlank}")
    private String originOutOrderNo;

    /// 退款金额(单位: 分, 有赞接口 refund_fee 单位同为分, 直接透传)
    @NotNull(message = "{validation.field.amount.notNull}")
    @Positive(message = "{validation.field.amount.positive}")
    private Long amount;

    /// 退款异步通知地址(notify_url)
    private String notifyUrl;
}
