package org.dromara.daxpay.channel.alipay.strategy.sub;

import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.service.strategy.AbsGatewayPayStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 支付宝网关支付策略
 * @author xxm
 * @since 2025/3/27
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AlipaySubGatewayPayStrategy extends AbsGatewayPayStrategy {

    /**
     * 策略标识, 可以自行进行扩展
     *
     * @see ChannelEnum
     */
    @Override
    public String getChannel() {
        return ChannelEnum.ALIPAY_ISV.getCode();
    }
}
