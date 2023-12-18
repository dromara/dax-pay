package cn.bootx.platform.daxpay.core.payment.sync.factory;


import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.core.payment.sync.func.AbsPaySyncStrategy;
import cn.bootx.platform.daxpay.core.payment.sync.strategy.AliPaySyncStrategy;
import cn.bootx.platform.daxpay.core.payment.sync.strategy.WeChatPaySyncStrategy;
import cn.bootx.platform.daxpay.exception.pay.PayUnsupportedMethodException;
import cn.hutool.extra.spring.SpringUtil;

/**
 * 支付同步策略工厂类
 * @author xxm
 * @since 2023/7/14
 */
public class PaySyncStrategyFactory {
    /**
     * 获取支付同步策略
     * @param payChannelCode 支付通道编码
     * @return 支付同步策略类
     */
    public static AbsPaySyncStrategy create(String payChannelCode) {
        AbsPaySyncStrategy strategy;
        PayChannelEnum channelEnum = PayChannelEnum.findByCode(payChannelCode);
        switch (channelEnum) {
            case ALI:
                strategy = SpringUtil.getBean(AliPaySyncStrategy.class);
                break;
            case WECHAT:
                strategy = SpringUtil.getBean(WeChatPaySyncStrategy.class);
                break;
            default:
                throw new PayUnsupportedMethodException();
        }
        // noinspection ConstantConditions
        return strategy;
    }
}
