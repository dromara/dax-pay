package cn.bootx.platform.daxpay.core.refund.func;

import cn.bootx.platform.daxpay.core.payment.entity.Payment;

import java.util.List;

/**
 * 支付退款策略接口
 * @author xxm
 * @since 2023/7/5
 */
@FunctionalInterface
public interface PayRefundStrategyConsumer<T extends List<AbsPayRefundStrategy>, S extends Payment> {

    void accept(T t, S s);

}
