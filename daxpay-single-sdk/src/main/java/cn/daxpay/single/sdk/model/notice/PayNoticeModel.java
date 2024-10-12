package cn.daxpay.single.sdk.model.notice;

import cn.daxpay.single.sdk.code.ChannelEnum;
import cn.daxpay.single.sdk.code.PayAllocStatusEnum;
import cn.daxpay.single.sdk.code.PayRefundStatusEnum;
import cn.daxpay.single.sdk.code.PayStatusEnum;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 支付异步通知类
 * @author xxm
 * @since 2024/1/7
 */
@Data
public class PayNoticeModel{

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

    /** 是否开启自动分账,*/
    private Boolean autoAllocation;
    /**
     * 支付通道
     * @see ChannelEnum
     */
    private String channel;

    /** 支付方式 */
    private String method;

    /** 支付金额 */
    private BigDecimal amount;

    /**
     * 支付状态
     * @see PayStatusEnum
     */
    private String status;


    /**
     * 退款状态
     * @see PayRefundStatusEnum
     */
    private String refundStatus;

    /**
     * 分账状态
     * @see PayAllocStatusEnum
     */
    private String allocStatus;

    /** 支付成功时间 */
    private String payTime;

    /** 过期时间 */
    private String expiredTime;

    /** 支付关闭时间 */
    private String closeTime;

    /** 支付创建时间 */
    private String createTime;

    /** 商户扩展参数,回调时会原样返回 */
    private String attach;

    /** 错误码 */
    private String errorCode;

    /** 错误原因 */
    private String errorMsg;

}
