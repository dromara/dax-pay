package cn.bootx.platform.daxpay.service.common.context;

import cn.bootx.platform.daxpay.code.PayWayEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 异步支付信息，不只局限在支付流程，同步、退款、回调中都会用到
 * @author xxm
 * @since 2021/2/28
 */
@Data
@Accessors(chain = true)
public class AsyncPayLocal {

    /**
     * 异步支付方式
     * @see PayWayEnum
     */
    private String payWay;

    /**
     * 第三方支付平台订单号
     * 1. 如付款码支付直接成功时会出现
     * 2. 回调或者支付同步时也会有这个值
     */
    private String gatewayOrderNo;


    /** 支付参数体(通常用于发起支付的参数) */
    private String payBody;

    /** 订单失效时间, 优先用这个 */
    private LocalDateTime expiredTime;

    /** 支付完成时间(通常用于接收异步支付返回的时间) */
    private LocalDateTime payTime;

}
