package cn.bootx.platform.daxpay.common.context;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 异步支付信息
 * @author xxm
 * @since 2021/2/28
 */
@Data
@Accessors(chain = true)
public class AsyncPayLocal {

    /** 支付参数体(通常用于发起支付的参数) */
    private String payBody;

    /** 第三方支付平台订单号(如付款码支付直接成功时会出现) */
    private String tradeNo;

    /** 订单失效时间, 优先用这个 */
    private LocalDateTime expiredTime;

}
