package org.dromara.daxpay.channel.alipay.strategy.merchant;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.service.strategy.AbsCheckoutStrategy;
import org.springframework.stereotype.Component;

/**
 * 支付宝
 * @author xxm
 * @since 2024/12/2
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AliPayCheckoutStrategy extends AbsCheckoutStrategy {

    /**
     * 策略标识, 可以自行进行扩展
     *
     * @see ChannelEnum
     */
    @Override
    public String getChannel() {
        return ChannelEnum.ALIPAY.getCode();
    }

    /**
     * 检测付款码
     */
    @Override
    public boolean checkBarCode(String barCode){
        String[] ali = { "25", "26", "27", "28", "29", "30" };
        return StrUtil.startWithAny(barCode.substring(0, 2), ali);
    }
}
