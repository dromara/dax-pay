package cn.bootx.platform.daxpay.func;

import cn.bootx.platform.daxpay.core.order.pay.entity.PayOrder;

import java.util.List;

/**
 * 支付策略接口
 *
 * @author xxm
 * @since 2020/12/9
 */
@FunctionalInterface
public interface PayStrategyConsumer<T extends List<AbsPayStrategy>, S extends PayOrder> {

    void accept(T t, S s);

}
