package cn.bootx.platform.daxpay.service.core.payment.close.factory;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.exception.pay.PayUnsupportedMethodException;
import cn.bootx.platform.daxpay.service.core.payment.close.strategy.AliPayCloseStrategy;
import cn.bootx.platform.daxpay.service.core.payment.close.strategy.UnionPayCloseStrategy;
import cn.bootx.platform.daxpay.service.core.payment.close.strategy.WeChatPayCloseStrategy;
import cn.bootx.platform.daxpay.service.func.AbsPayCloseStrategy;
import cn.hutool.extra.spring.SpringUtil;
import lombok.experimental.UtilityClass;

/**
 * 支付关闭策略工厂
 * @author xxm
 * @since 2023/12/29
 */
@UtilityClass
public class PayCloseStrategyFactory {

    /**
     * 根据传入的支付通道创建策略
     * @return 支付策略
     */
    public static AbsPayCloseStrategy create(String channel) {
        PayChannelEnum channelEnum = PayChannelEnum.findByCode(channel);
        AbsPayCloseStrategy strategy;
        switch (channelEnum) {
            case ALI:
                strategy = SpringUtil.getBean(AliPayCloseStrategy.class);
                break;
            case WECHAT:
                strategy = SpringUtil.getBean(WeChatPayCloseStrategy.class);
                break;
            case UNION_PAY:
                strategy = SpringUtil.getBean(UnionPayCloseStrategy.class);
                break;
            default:
                throw new PayUnsupportedMethodException();
        }
        return strategy;
    }
}
