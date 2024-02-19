package cn.bootx.platform.daxpay.demo.result;

import cn.bootx.platform.daxpay.sdk.code.PayChannelEnum;
import cn.bootx.platform.daxpay.sdk.code.PayStatusEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 发起支付后响应对象
 * @author xxm
 * @since 2024/2/8
 */
@Getter
@Setter
public class PayOrderResult {
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
