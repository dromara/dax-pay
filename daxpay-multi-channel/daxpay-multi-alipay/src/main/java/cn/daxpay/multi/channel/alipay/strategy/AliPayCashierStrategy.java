package cn.daxpay.multi.channel.alipay.strategy;

import cn.daxpay.multi.core.enums.ChannelEnum;
import cn.daxpay.multi.service.strategy.AbsChannelCashierStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 支付宝收银台支付
 * @author xxm
 * @since 2024/9/29
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AliPayCashierStrategy extends AbsChannelCashierStrategy {

    /**
     * 策略标识, 可以自行进行扩展
     */
    @Override
    public String getChannel() {
        return ChannelEnum.ALI.getCode();
    }
}
