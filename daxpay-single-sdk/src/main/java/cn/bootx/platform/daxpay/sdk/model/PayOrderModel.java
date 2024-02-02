package cn.bootx.platform.daxpay.sdk.model;

import cn.bootx.platform.daxpay.sdk.code.PayChannelEnum;
import cn.bootx.platform.daxpay.sdk.code.PayStatusEnum;
import cn.bootx.platform.daxpay.sdk.net.DaxPayResponseModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 支付相应对象
 * @author xxm
 * @since 2024/2/2
 */
@Getter
@Setter
@ToString
public class PayOrderModel extends DaxPayResponseModel {

    /** 支付ID */
    private Long paymentId;

    /** 是否是异步支付 */
    private boolean asyncPay;

    /**
     * 异步支付通道
     * @see PayChannelEnum
     */
    private String asyncChannel;


    /** 支付参数体(通常用于发起异步支付的参数) */
    private String payBody;

    /**
     * 支付状态
     * @see PayStatusEnum
     */
    private String status;
}
