package cn.bootx.platform.daxpay.service.core.payment.sync.factory;


import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.exception.pay.PayUnsupportedMethodException;
import cn.bootx.platform.daxpay.service.core.payment.sync.strategy.pay.AliPaySyncStrategy;
import cn.bootx.platform.daxpay.service.core.payment.sync.strategy.pay.UnionPaySyncStrategy;
import cn.bootx.platform.daxpay.service.core.payment.sync.strategy.pay.WeChatPaySyncStrategy;
import cn.bootx.platform.daxpay.service.func.AbsPaySyncStrategy;
import cn.hutool.extra.spring.SpringUtil;

/**
 * 支付同步策略工厂类
 * @author xxm
 * @since 2023/7/14
 */
public class PaySyncStrategyFactory {
    /**
     * 获取支付同步策略, 只有异步支付方式才需要这个功能
     * @param channelCode 支付通道编码
     * @return 支付同步策略类
     */
    public static AbsPaySyncStrategy create(String channelCode) {
        AbsPaySyncStrategy strategy;
        PayChannelEnum channelEnum = PayChannelEnum.findByCode(channelCode);
        switch (channelEnum) {
            case ALI:
                strategy = SpringUtil.getBean(AliPaySyncStrategy.class);
                break;
            case WECHAT:
                strategy = SpringUtil.getBean(WeChatPaySyncStrategy.class);
                break;
            case UNION_PAY:
                strategy = SpringUtil.getBean(UnionPaySyncStrategy.class);
                break;
            default:
                throw new PayUnsupportedMethodException();
        }
        // noinspection ConstantConditions
        return strategy;
    }
}
