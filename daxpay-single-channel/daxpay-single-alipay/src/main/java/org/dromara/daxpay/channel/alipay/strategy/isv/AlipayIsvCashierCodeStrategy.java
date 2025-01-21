package org.dromara.daxpay.channel.alipay.strategy.isv;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.service.strategy.AbsCashierCodeStrategy;
import org.springframework.stereotype.Component;

/**
 * 支付宝收银码牌支付
 * @author xxm
 * @since 2024/9/29
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AlipayIsvCashierCodeStrategy extends AbsCashierCodeStrategy {

    /**
     * 策略标识, 可以自行进行扩展
     */
    @Override
    public String getChannel() {
        return ChannelEnum.ALIPAY_ISV.getCode();
    }
}
