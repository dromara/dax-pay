package cn.bootx.platform.daxpay.service.core.payment.pay.factory;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.exception.pay.PayUnsupportedMethodException;
import cn.bootx.platform.daxpay.service.core.payment.pay.strategy.AliPayStrategy;
import cn.bootx.platform.daxpay.service.core.payment.pay.strategy.UnionPayStrategy;
import cn.bootx.platform.daxpay.service.core.payment.pay.strategy.WalletPayStrategy;
import cn.bootx.platform.daxpay.service.core.payment.pay.strategy.WeChatPayStrategy;
import cn.bootx.platform.daxpay.service.func.AbsPayStrategy;
import cn.hutool.extra.spring.SpringUtil;
import lombok.experimental.UtilityClass;


/**
 * 支付策略工厂
 *
 * @author xxm
 * @since 2020/12/11
 */
@UtilityClass
public class PayStrategyFactory {

    /**
     * 根据传入的支付通道创建策略
     * @return 支付策略
     */
    public AbsPayStrategy create(String channel) {
        AbsPayStrategy strategy;
        PayChannelEnum channelEnum = PayChannelEnum.findByCode(channel);
        switch (channelEnum) {
            case ALI:
                strategy = SpringUtil.getBean(AliPayStrategy.class);
                break;
            case WECHAT:
                strategy = SpringUtil.getBean(WeChatPayStrategy.class);
                break;
            case UNION_PAY:
                strategy = SpringUtil.getBean(UnionPayStrategy.class);
                break;
            case WALLET:
                strategy = SpringUtil.getBean(WalletPayStrategy.class);
                break;
            default:
                throw new PayUnsupportedMethodException();
        }
        return strategy;
    }
}
