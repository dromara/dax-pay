package cn.bootx.platform.daxpay.sdk.model.pay;

import cn.bootx.platform.daxpay.sdk.code.PayChannelEnum;
import cn.bootx.platform.daxpay.sdk.code.PayStatusEnum;
import cn.bootx.platform.daxpay.sdk.net.DaxPayResponseModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 支付订单查询响应参数
 * @author xxm
 * @since 2024/1/16
 */
@Getter
@Setter
@ToString
public class QueryPayOrderModel extends DaxPayResponseModel {

    /** 支付ID */
    private Long paymentId;

    /** 业务号 */
    private String businessNo;

    /** 标题 */
    private String title;

    /** 描述 */
    private String description;

    /** 是否是异步支付 */
    private boolean asyncPay;

    /** 是否是组合支付 */
    private boolean combinationPay;

    /**
     * 异步支付通道
     * @see PayChannelEnum
     */
    private String asyncChannel;

    /** 金额 */
    private Integer amount;

    /** 可退款余额 */
    private Integer refundableBalance;

    /**
     * 支付状态
     * @see PayStatusEnum
     */
    private String status;

    /** 支付时间(秒级时间戳) */
    private Integer payTime;

    /** 过期时间(秒级时间戳) */
    private Integer expiredTime;

    /** 支付通道列表 */
    private List<QueryPayChannelOrder> channels;
}
