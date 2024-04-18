package cn.bootx.platform.daxpay.service.core.payment.allocation.factory;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.exception.pay.PayUnsupportedMethodException;
import cn.bootx.platform.daxpay.service.core.payment.allocation.strategy.allocation.AliPayAllocationStrategy;
import cn.bootx.platform.daxpay.service.core.payment.allocation.strategy.allocation.WeChatPayAllocationStrategy;
import cn.bootx.platform.daxpay.service.func.AbsAllocationStrategy;
import cn.hutool.extra.spring.SpringUtil;
import lombok.experimental.UtilityClass;

/**
 * 分账策略工厂
 * @author xxm
 * @since 2024/4/7
 */
@UtilityClass
public class AllocationFactory {

    /**
     * 根据传入的支付通道创建策略
     * @return 支付策略
     */
    public static AbsAllocationStrategy create(String channel) {
        PayChannelEnum channelEnum = PayChannelEnum.findByCode(channel);

        AbsAllocationStrategy strategy;
        switch (channelEnum) {
            case ALI:
                strategy = SpringUtil.getBean(AliPayAllocationStrategy.class);
                break;
            case WECHAT:
                strategy = SpringUtil.getBean(WeChatPayAllocationStrategy.class);
                break;
            default:
                throw new PayUnsupportedMethodException();
        }
        return strategy;
    }
}
