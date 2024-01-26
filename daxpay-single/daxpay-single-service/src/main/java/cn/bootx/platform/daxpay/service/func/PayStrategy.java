package cn.bootx.platform.daxpay.service.func;

import cn.bootx.platform.daxpay.code.PayChannelEnum;

/**
 * 支付相关策略标识接口
 * @author xxm
 * @since 2023/12/27
 */
public interface PayStrategy {

    /**
     * 策略标识
     */
    PayChannelEnum getChannel();
}
