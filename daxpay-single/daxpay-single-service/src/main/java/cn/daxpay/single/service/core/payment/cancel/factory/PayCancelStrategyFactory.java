package cn.daxpay.single.service.core.payment.cancel.factory;

import cn.daxpay.single.code.PayChannelEnum;
import cn.daxpay.single.exception.pay.PayUnsupportedMethodException;
import cn.daxpay.single.service.core.payment.cancel.strategy.AliPayCancelStrategy;
import cn.daxpay.single.service.core.payment.cancel.strategy.WeChatPayCancelStrategy;
import cn.daxpay.single.service.func.AbsPayCancelStrategy;
import cn.hutool.extra.spring.SpringUtil;
import lombok.experimental.UtilityClass;

/**
 * 订单撤销策略工厂
 * @author xxm
 * @since 2024/6/5
 */
@UtilityClass
public class PayCancelStrategyFactory {

    /**
     * 根据传入的支付通道创建策略
     * @return 支付策略
     */
    public static AbsPayCancelStrategy create(String channel) {
        PayChannelEnum channelEnum = PayChannelEnum.findByCode(channel);
        AbsPayCancelStrategy strategy;
        switch (channelEnum) {
            case ALI:
                strategy = SpringUtil.getBean(AliPayCancelStrategy.class);
                break;
            case WECHAT:
                strategy = SpringUtil.getBean(WeChatPayCancelStrategy.class);
                break;
            default:
                throw new PayUnsupportedMethodException();
        }
        return strategy;
    }
}
