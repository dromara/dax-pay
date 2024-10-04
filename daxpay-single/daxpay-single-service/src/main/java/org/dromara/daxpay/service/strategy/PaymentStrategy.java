package org.dromara.daxpay.service.strategy;


import org.dromara.daxpay.core.enums.ChannelEnum;

/**
 * 支付相关策略标识接口
 * @author xxm
 * @since 2023/12/27
 */
public interface PaymentStrategy {

    /**
     * 策略标识, 可以自行进行扩展
     * @see ChannelEnum
     */
    String getChannel();
}
