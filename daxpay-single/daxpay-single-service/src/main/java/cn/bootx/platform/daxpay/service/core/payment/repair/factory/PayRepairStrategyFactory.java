package cn.bootx.platform.daxpay.service.core.payment.repair.factory;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.exception.pay.PayUnsupportedMethodException;
import cn.bootx.platform.daxpay.service.core.payment.repair.strategy.pay.AliPayRepairStrategy;
import cn.bootx.platform.daxpay.service.core.payment.repair.strategy.pay.UnionPayRepairStrategy;
import cn.bootx.platform.daxpay.service.core.payment.repair.strategy.pay.WeChatPayRepairStrategy;
import cn.bootx.platform.daxpay.service.func.AbsPayRepairStrategy;
import cn.hutool.extra.spring.SpringUtil;
import lombok.experimental.UtilityClass;

/**
 * 支付修复策略工厂类
 * @author xxm
 * @since 2023/12/29
 */
@UtilityClass
public class PayRepairStrategyFactory {
    /**
     * 根据传入的支付通道创建策略
     * @return 支付修复策略
     */
    public static AbsPayRepairStrategy create(String channel) {
        PayChannelEnum channelEnum = PayChannelEnum.findByCode(channel);
        AbsPayRepairStrategy strategy;
        switch (channelEnum) {
            case ALI:
                strategy = SpringUtil.getBean(AliPayRepairStrategy.class);
                break;
            case WECHAT:
                strategy = SpringUtil.getBean(WeChatPayRepairStrategy.class);
                break;
            case UNION_PAY:
                strategy = SpringUtil.getBean(UnionPayRepairStrategy.class);
                break;
            default:
                throw new PayUnsupportedMethodException();
        }
        return strategy;
    }

}
