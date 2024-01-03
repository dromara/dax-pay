package cn.bootx.platform.daxpay.service.func;

import cn.bootx.platform.daxpay.service.core.record.pay.entity.PayOrder;

import java.util.List;

/**
 * 支付策略消费者接口
 *
 * @author xxm
 * @since 2020/12/9
 */
@FunctionalInterface
public interface PayStrategyConsumer<T extends List<? extends PayStrategy>, S extends PayOrder> {

    /**
     * 消费者接口
     * @param t 策略结论
     * @param s
     */
    void accept(T t, S s);

}
