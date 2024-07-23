package cn.daxpay.multi.sdk.result.trade.pay;

import cn.daxpay.multi.sdk.code.PayChannelEnum;
import cn.daxpay.multi.sdk.code.PayOrderAllocStatusEnum;
import cn.daxpay.multi.sdk.code.PayOrderRefundStatusEnum;
import cn.daxpay.multi.sdk.code.PayStatusEnum;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 支付订单查询响应参数
 * @author xxm
 * @since 2024/1/16
 */
@Data
public class PayOrderModel{

    /** 订单号 */
    private String orderNo;

    /** 商户订单号 */
    private String bizOrderNo;

    /** 通道系统交易号 */
    private String outOrderNo;

    /** 标题 */
    private String title;

    /** 描述 */
    private String description;

    /** 是否支持分账 */
    private Boolean allocation;

    /** 是否开启自动分账, 不传输为不开启 */
    private Boolean autoAllocation;

    /**
     * 支付通道
     * @see PayChannelEnum
     */
    private String channel;

    /**
     * 支付方式
     */
    private String method;

    /** 金额 */
    private BigDecimal amount;

    /** 可退款余额 */
    private BigDecimal refundableBalance;

    /**
     * 支付状态
     * @see PayStatusEnum
     */
    private String status;

    /**
     * 退款状态
     * @see PayOrderRefundStatusEnum
     */
    private String refundStatus;

    /**
     * 分账状态
     * @see PayOrderAllocStatusEnum
     */
    private String allocStatus;


    /** 支付时间 */
    private Long payTime;

    /** 过期时间 */
    private Long expiredTime;

    /** 关闭时间 */
    private Long closeTime;

    /** 错误码 */
    private String errorCode;

    /** 错误信息 */
    private String errorMsg;
}
