package org.dromara.daxpay.single.sdk.model.notice;

import org.dromara.daxpay.single.sdk.code.ChannelEnum;
import org.dromara.daxpay.single.sdk.code.RefundStatusEnum;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 退款通知消息
 * @author xxm
 * @since 2024/2/22
 */
@Data
public class RefundNoticeModel{

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
     * @see ChannelEnum
     */
    private String channel;

    /** 订单金额 */
    private Integer orderAmount;

    /** 退款金额 */
    private BigDecimal amount;

    /** 退款原因 */
    private String reason;

    /** 退款成功时间 */
    private Long finishTime;

    /** 退款创建时间 */
    private Long createTime;
    /**
     * 退款状态
     * @see RefundStatusEnum
     */
    private String status;

    /** 商户扩展参数,回调时会原样返回 */
    private String attach;

    /** 错误码 */
    private String errorCode;

    /** 错误原因 */
    private String errorMsg;
}
