package cn.daxpay.single.sdk.model.refund;

import cn.daxpay.single.sdk.code.PayChannelEnum;
import cn.daxpay.single.sdk.code.RefundStatusEnum;
import cn.daxpay.single.sdk.net.DaxPayResponseModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 退款订单数据
 * @author xxm
 * @since 2024/1/16
 */
@Getter
@Setter
@ToString
public class RefundOrderModel extends DaxPayResponseModel {


    /** 支付订单号 */
    private String orderNo;

    /** 商户支付订单号 */
    private String bizOrderNo;

    /** 通道支付订单号 */
    private String outOrderNo;

    /** 支付标题 */
    private String title;

    /** 退款号 */
    private String refundNo;

    /** 商户退款号 */
    private String bizRefundNo;

    /** 通道退款交易号 */
    private String outRefundNo;

    /**
     * 退款通道
     * @see PayChannelEnum
     */
    private String channel;

    /** 订单金额 */
    private Integer orderAmount;

    /** 退款金额 */
    private BigDecimal amount;

    /** 退款原因 */
    private String reason;

    /** 退款发起时间 */
    private Long refundTime;

    /** 退款完成时间 */
    private Long finishTime;

    /**
     * 退款状态
     * @see RefundStatusEnum
     */
    private String status;

    /** 错误码 */
    private String errorCode;

    /** 错误信息 */
    private String errorMsg;
}
