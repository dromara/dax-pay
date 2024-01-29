package cn.bootx.platform.daxpay.service.core.payment.sync.factory;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.exception.pay.PayUnsupportedMethodException;
import cn.bootx.platform.daxpay.service.core.payment.sync.strategy.Refund.AliRefundSyncStrategy;
import cn.bootx.platform.daxpay.service.core.payment.sync.strategy.Refund.WeChatRefundSyncStrategy;
import cn.bootx.platform.daxpay.service.func.AbsRefundSyncStrategy;
import cn.hutool.extra.spring.SpringUtil;
import lombok.experimental.UtilityClass;

/**
 * 支付退款同步策略工厂
 * @author xxm
 * @since 2024/1/29
 */
@UtilityClass
public class RefundSyncStrategyFactory {
    /**
     * 获取支付同步策略, 只有异步支付方式才需要这个功能
     * @param channelCode 支付通道编码
     * @return 支付同步策略类
     */
    public static AbsRefundSyncStrategy create(String channelCode) {
        AbsRefundSyncStrategy strategy;
        PayChannelEnum channelEnum = PayChannelEnum.findByCode(channelCode);
        switch (channelEnum) {
            case ALI:
                strategy = SpringUtil.getBean(AliRefundSyncStrategy.class);
                break;
            case WECHAT:
                strategy = SpringUtil.getBean(WeChatRefundSyncStrategy.class);
                break;
            default:
                throw new PayUnsupportedMethodException();
        }
        // noinspection ConstantConditions
        return strategy;
    }
}
