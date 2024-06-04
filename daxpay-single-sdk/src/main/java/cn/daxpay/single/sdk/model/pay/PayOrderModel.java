package cn.daxpay.single.sdk.model.pay;

import cn.daxpay.single.sdk.code.PayChannelEnum;
import cn.daxpay.single.sdk.code.PayOrderAllocStatusEnum;
import cn.daxpay.single.sdk.code.PayStatusEnum;
import cn.daxpay.single.sdk.net.DaxPayResponseModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 支付订单查询响应参数
 * @author xxm
 * @since 2024/1/16
 */
@Getter
@Setter
@ToString(callSuper = true)
public class PayOrderModel extends DaxPayResponseModel {


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
    private Integer amount;

    /** 可退款余额 */
    private Integer refundableBalance;

    /**
     * 支付状态
     * @see PayStatusEnum
     */
    private String status;

    /**
     * 分账状态
     * @see PayOrderAllocStatusEnum
     */
    private String allocationStatus;


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
