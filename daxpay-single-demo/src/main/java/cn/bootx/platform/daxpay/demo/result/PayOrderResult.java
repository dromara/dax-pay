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

    /** 商户订单号 */
    private String bizOrderNo;

    /** 订单号 */
    private String orderNo;

    /**
     * 支付状态
     * @see PayStatusEnum
     */
    private String status;

    /** 支付参数体(通常用于发起支付的参数) */
    private String payBody;

}
