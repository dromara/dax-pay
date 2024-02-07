package cn.bootx.platform.daxpay.sdk.model.refund;

import cn.bootx.platform.daxpay.sdk.code.RefundStatusEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 支付退款通道明细数据
 * @author xxm
 * @since 2024/1/17
 */
@Getter
@Setter
@ToString
public class QueryRefundChannelOrderModel {

    /** 通道 */
    private String channel;

    /** 通道支付单id */
    private Long payChannelId;

    /** 异步支付方式 */
    private boolean async;

    /** 订单金额 */
    private Integer orderAmount;

    /** 退款金额 */
    private Integer amount;

    /** 剩余可退余额 */
    private Integer refundableAmount;

    /**
     * 退款状态
     * @see RefundStatusEnum
     */
    private String status;

    /** 退款完成时间(秒级时间戳) */
    private Long refundTime;
}
