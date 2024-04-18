package cn.bootx.platform.daxpay.service.core.payment.allocation.factory;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.exception.pay.PayUnsupportedMethodException;
import cn.bootx.platform.daxpay.service.core.payment.allocation.strategy.receiver.AliPayAllocationReceiverStrategy;
import cn.bootx.platform.daxpay.service.core.payment.allocation.strategy.receiver.WeChatPayAllocationReceiverStrategy;
import cn.bootx.platform.daxpay.service.func.AbsAllocationReceiverStrategy;
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
