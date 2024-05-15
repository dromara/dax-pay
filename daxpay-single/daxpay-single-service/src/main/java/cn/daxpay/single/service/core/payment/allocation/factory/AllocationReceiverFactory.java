package cn.daxpay.single.service.core.payment.allocation.factory;

import cn.daxpay.single.code.PayChannelEnum;
import cn.daxpay.single.exception.pay.PayUnsupportedMethodException;
import cn.daxpay.single.service.core.payment.allocation.strategy.receiver.AliPayAllocationReceiverStrategy;
import cn.daxpay.single.service.core.payment.allocation.strategy.receiver.WeChatPayAllocationReceiverStrategy;
import cn.daxpay.single.service.func.AbsAllocationReceiverStrategy;
import cn.hutool.extra.spring.SpringUtil;
import lombok.experimental.UtilityClass;

/**
 * 分账接收方策略工厂
 * @author xxm
 * @since 2024/4/1
 */
@UtilityClass
public class AllocationReceiverFactory {

    /**
     * 根据传入的支付通道创建策略
     * @return 支付策略
     */
    public static AbsAllocationReceiverStrategy create(PayChannelEnum channelEnum) {

        AbsAllocationReceiverStrategy strategy;
        switch (channelEnum) {
            case ALI:
                strategy = SpringUtil.getBean(AliPayAllocationReceiverStrategy.class);
                break;
            case WECHAT:
                strategy = SpringUtil.getBean(WeChatPayAllocationReceiverStrategy.class);
                break;
            default:
                throw new PayUnsupportedMethodException();
        }
        return strategy;
    }
}
