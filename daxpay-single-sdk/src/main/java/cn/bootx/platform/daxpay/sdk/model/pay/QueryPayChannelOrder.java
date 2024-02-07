package cn.bootx.platform.daxpay.sdk.model.pay;

import cn.bootx.platform.daxpay.sdk.code.PayStatusEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 支付订单通道响应参数
 * @author xxm
 * @since 2024/1/16
 */
@Getter
@Setter
@ToString
public class QueryPayChannelOrder {

    /** 是否为异步支付通道 */
    private boolean async;

    /** 支付通道 */
    private String channel;

    /** 支付方式 */
    private String payWay;

    /**
     * 网关订单号, 用与将记录关联起来
     */
    private String gatewayOrderNo;

    /**
     * 支付状态
     * @see PayStatusEnum
     */
    private String status;

    /** 金额 */
    private Integer amount;

    /** 可退款金额 */
    private Integer refundableBalance;
}
