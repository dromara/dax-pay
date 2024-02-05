package cn.bootx.platform.daxpay.sdk.model.refund;

import cn.bootx.platform.daxpay.sdk.net.DaxPayResponseModel;
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
public class RefundChannelOrderModel extends DaxPayResponseModel {

    /** 通道 */
    private String channel;

    /** 通道支付单id */
    private Long payChannelId;

    /** 异步支付方式 */
    private boolean async;

    /** 订单金额" */
    private Integer orderAmount;

    /** 退款金额 */
    private Integer amount;
}
