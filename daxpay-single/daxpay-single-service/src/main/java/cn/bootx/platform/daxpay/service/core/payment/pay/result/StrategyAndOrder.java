package cn.bootx.platform.daxpay.service.core.payment.pay.result;

import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.func.AbsPayStrategy;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * 策略和订单
 * @author xxm
 * @since 2024/3/21
 */
@Getter
@AllArgsConstructor
public class StrategyAndOrder {

    /** 支付订单 */
    private final PayOrder order;

    /** 支付策略集合 */
    private final List<AbsPayStrategy> strategies;

}
