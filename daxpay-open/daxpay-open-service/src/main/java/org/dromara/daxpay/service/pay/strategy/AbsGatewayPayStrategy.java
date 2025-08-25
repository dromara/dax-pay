package org.dromara.daxpay.service.pay.strategy;

import org.dromara.daxpay.core.param.gateway.GatewayCashierPayParam;
import org.dromara.daxpay.core.param.trade.pay.PayParam;

/**
 * 抽象网关支付策略
 * @author xxm
 * @since 2024/12/2
 */
public abstract class AbsGatewayPayStrategy implements PaymentStrategy{

    /**
     * 支付参数处理
     */
    public void handlePayParam(GatewayCashierPayParam cashierPayParam, PayParam payParam) {
    }
}
