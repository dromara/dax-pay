package cn.bootx.platform.daxpay.core.payment.refund.func;

import cn.bootx.platform.daxpay.core.order.pay.entity.PayOrder;

import java.util.List;

/**
 * 支付退款策略接口
 * @author xxm
 * @since 2023/7/5
 */
@FunctionalInterface
public interface PayRefundStrategyConsumer<T extends List<AbsPayRefundStrategy>, S extends PayOrder> {

    void accept(T t, S s);

}
