package cn.daxpay.open.channel.fuyou.client.req;

import cn.daxpay.open.channel.fuyou.client.credential.FuyouDirectSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.OffsetDateTime;

/// # 富友直连退款请求
@Data
public class FuyouDirectRefundReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private FuyouDirectSdkCredential credential;

    /// 关联订单号(原支付订单的富友 mchnt_order_no)
    @NotBlank(message = "{validation.field.relationOrderNo.notBlank}")
    private String relationOrderNo;

    /// 支付厂商(WECHAT/ALIPAY/UNIONPAY, 对应 order_type)
    @NotBlank(message = "{validation.field.tradeProduct.notBlank}")
    private String tradeProduct;

    /// 退款单号(平台退款交易号, 作为富友 refund_order_no)
    @NotBlank(message = "{validation.field.outRefundNo.notBlank}")
    private String outRefundNo;

    /// 原订单总金额(单位: 分)
    @NotNull(message = "{validation.field.amount.notNull}")
    @Positive(message = "{validation.field.amount.positive}")
    private Long totalAmount;

    /// 退款金额(单位: 分)
    @NotNull(message = "{validation.field.refundAmount.notNull}")
    @Positive(message = "{validation.field.refundAmount.positive}")
    private Long refundAmount;

    /// 退款原因
    private String reason;

    /// 原支付完成时间(用于 reserved_origi_dt, yyyyMMdd; 填写后支持 180 天内交易退款)
    private OffsetDateTime originPayTime;
}
