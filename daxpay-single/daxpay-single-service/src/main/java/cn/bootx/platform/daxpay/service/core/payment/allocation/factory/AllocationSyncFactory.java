package cn.bootx.platform.daxpay.service.core.payment.allocation.factory;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.exception.pay.PayUnsupportedMethodException;
import cn.bootx.platform.daxpay.service.core.payment.sync.strategy.allocation.AliPayAllocationSyncStrategy;
import cn.bootx.platform.daxpay.service.core.payment.sync.strategy.allocation.WechatPayAllocationSyncStrategy;
import cn.bootx.platform.daxpay.service.func.AbsAllocationSyncStrategy;
import cn.hutool.extra.spring.SpringUtil;
import lombok.experimental.UtilityClass;

/**
 * 分账同步策略工厂
 * @author xxm
 * @since 2024/4/12
 */
@UtilityClass
public class AllocationSyncFactory {

    /**
     * 根据传入的支付通道创建策略
     * @return 支付策略
     */
    public static AbsAllocationSyncStrategy create(String channel) {
        PayChannelEnum channelEnum = PayChannelEnum.findByCode(channel);

        AbsAllocationSyncStrategy strategy;
        switch (channelEnum) {
            case ALI:
                strategy = SpringUtil.getBean(AliPayAllocationSyncStrategy.class);
                break;
            case WECHAT:
                strategy = SpringUtil.getBean(WechatPayAllocationSyncStrategy.class);
                break;
            default:
                throw new PayUnsupportedMethodException();
        }
        return strategy;
    }
}
