package cn.daxpay.single.result.order;

import cn.daxpay.single.code.PayChannelEnum;
import cn.daxpay.single.code.RefundStatusEnum;
import cn.daxpay.single.result.PaymentCommonResult;
import cn.daxpay.single.serializer.LocalDateTimeToTimestampSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 退款订单数据
 * @author xxm
 * @since 2024/1/16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "退款订单数据")
public class RefundOrderResult extends PaymentCommonResult {

    /** 支付订单号 */
    @Schema(description = "支付订单号")
    private String orderNo;

    /** 商户支付订单号 */
    @Schema(description = "商户支付订单号")
    private String bizOrderNo;

    /** 通道支付订单号 */
    @Schema(description = "通道支付订单号")
    private String outOrderNo;

    /** 支付标题 */
    @Schema(description = "支付标题")
    private String title;

    /** 退款号 */
    @Schema(description = "退款号")
    private String refundNo;

    /** 商户退款号 */
    @Schema(description = "商户退款号")
    private String bizRefundNo;

    /** 通道退款交易号 */
    @Schema(description = "通道退款交易号")
    private String outRefundNo;

    /**
     * 退款通道
     * @see PayChannelEnum
     */
    @Schema(description = "支付通道")
    private String channel;

    /** 订单金额 */
    @Schema(description = "订单金额")
    private Integer orderAmount;

    /** 退款金额 */
    @Schema(description = "退款金额")
    private Integer amount;

    /** 退款原因 */
    @Schema(description = "退款原因")
    private String reason;

    /** 退款发起时间 */
    @Schema(description = "退款发起时间")
    @JsonSerialize(using = LocalDateTimeToTimestampSerializer.class)
    private LocalDateTime refundTime;

    @Schema(description = "退款完成时间")
    @JsonSerialize(using = LocalDateTimeToTimestampSerializer.class)
    private LocalDateTime finishTime;

    /**
     * 退款状态
     * @see RefundStatusEnum
     */
    @Schema(description = "退款状态")
    private String status;

    /** 错误码 */
    @Schema(description = "错误码")
    private String errorCode;

    /** 错误信息 */
    @Schema(description = "错误信息")
    private String errorMsg;
}
