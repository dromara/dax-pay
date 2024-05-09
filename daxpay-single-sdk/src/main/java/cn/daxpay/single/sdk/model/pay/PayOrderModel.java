package cn.daxpay.single.sdk.model.pay;

import cn.daxpay.single.sdk.code.PayChannelEnum;
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
@ToString
public class PayOrderModel extends DaxPayResponseModel {

    /** 支付订单号 */
    private String orderNo;

    /** 业务系统订单号 */
    private String bizOrderNo;

    /** 标题 */
    private String title;

    /** 描述 */
    private String description;

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

    /** 支付时间 */
    private Long payTime;

    /** 过期时间 */
    private Long expiredTime;

    /** 关闭时间 */
    private Long closeTime;
}
