package cn.bootx.platform.daxpay.service.core.payment.refund.factory;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.exception.pay.PayUnsupportedMethodException;
import cn.bootx.platform.daxpay.service.core.payment.refund.strategy.AliRefundStrategy;
import cn.bootx.platform.daxpay.service.core.payment.refund.strategy.UnionRefundStrategy;
import cn.bootx.platform.daxpay.service.core.payment.refund.strategy.WalletRefundStrategy;
import cn.bootx.platform.daxpay.service.core.payment.refund.strategy.WeChatRefundStrategy;
import cn.bootx.platform.daxpay.service.func.AbsRefundStrategy;
import cn.hutool.extra.spring.SpringUtil;

/**
 * 退款策略工厂
 * @author xxm
 * @since 2023/7/4
 */
public class RefundStrategyFactory {


    /**
     * 根据传入的支付通道创建策略
     * @return 支付策略
     */
    public static AbsRefundStrategy create(String channel) {

        AbsRefundStrategy strategy;
        PayChannelEnum channelEnum = PayChannelEnum.findByCode(channel);
        switch (channelEnum) {
            case ALI:
                strategy = SpringUtil.getBean(AliRefundStrategy.class);
                break;
            case WECHAT:
                strategy = SpringUtil.getBean(WeChatRefundStrategy.class);
                break;
            case UNION_PAY:
                strategy = SpringUtil.getBean(UnionRefundStrategy.class);
                break;
            case WALLET:
                strategy = SpringUtil.getBean(WalletRefundStrategy.class);
                break;
            default:
                throw new PayUnsupportedMethodException();
        }
        return strategy;
    }

}
