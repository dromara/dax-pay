package cn.daxpay.open.channel.fuyou.client.req;

import cn.daxpay.open.channel.fuyou.client.credential.FuyouRsaSdkCredential;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.OffsetDateTime;

/// # 富友 H5 聚合收银台退款请求(主应用侧)
@Data
@Schema(title = "富友 H5 聚合收银台退款请求")
public class FuyouH5RefundReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private FuyouRsaSdkCredential credential;

    /// 原支付订单号(富友 pay_order_id, 即下单时的平台支付交易号)
    @NotBlank(message = "{validation.field.relationOrderNo.notBlank}")
    private String relationOrderNo;

    /// 退款单号(平台退款关联号, 作为富友 refund_order_id)
    @NotBlank(message = "{validation.field.outRefundNo.notBlank}")
    private String outRefundNo;

    /// 退款金额(单位: 分)
    @NotNull(message = "{validation.field.refundAmount.notNull}")
    @Positive(message = "{validation.field.refundAmount.positive}")
    private Long refundAmount;

    /// 原支付下单时间(用于 pay_order_date, yyyyMMdd, 东八区)
    private OffsetDateTime originOrderTime;

    /// 退款结果异步通知地址(由子应用透传给富友 back_notify_url)
    private String notifyUrl;
}
