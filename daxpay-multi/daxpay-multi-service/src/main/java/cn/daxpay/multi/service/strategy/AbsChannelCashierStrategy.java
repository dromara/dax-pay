package cn.daxpay.multi.service.strategy;

import cn.daxpay.multi.core.param.cashier.CashierPayParam;
import cn.daxpay.multi.core.param.trade.pay.PayParam;

/**
 * 抽象通道收银台策略
 * @author xxm
 * @since 2024/9/28
 */
public abstract class AbsChannelCashierStrategy implements PaymentStrategy{

    /**
     * 支付参数处理
     */
    public abstract void handlePayParam(CashierPayParam cashierPayParam, PayParam payParam);

}
