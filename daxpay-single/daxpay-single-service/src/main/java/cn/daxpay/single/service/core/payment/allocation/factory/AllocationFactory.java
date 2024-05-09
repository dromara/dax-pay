package cn.daxpay.single.service.core.payment.allocation.factory;

import cn.daxpay.single.code.PayChannelEnum;
import cn.daxpay.single.exception.pay.PayUnsupportedMethodException;
import cn.daxpay.single.service.core.payment.allocation.strategy.allocation.AliPayAllocationStrategy;
import cn.daxpay.single.service.core.payment.allocation.strategy.allocation.WeChatPayAllocationStrategy;
import cn.daxpay.single.service.func.AbsAllocationStrategy;
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
