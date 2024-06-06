package cn.daxpay.single.service.core.payment.sync.factory;


import cn.daxpay.single.code.PayChannelEnum;
import cn.daxpay.single.exception.pay.PayUnsupportedMethodException;
import cn.daxpay.single.service.core.payment.sync.strategy.pay.AliPaySyncStrategy;
import cn.daxpay.single.service.core.payment.sync.strategy.pay.UnionPaySyncStrategy;
import cn.daxpay.single.service.core.payment.sync.strategy.pay.WeChatPaySyncStrategy;
import cn.daxpay.single.service.func.AbsPaySyncStrategy;
import cn.hutool.extra.spring.SpringUtil;
import lombok.experimental.UtilityClass;

/**
 * 支付同步策略工厂类
 * @author xxm
 * @since 2023/7/14
 */
@UtilityClass
public class PaySyncStrategyFactory {
    /**
     * 获取支付同步策略
     * @param channelCode 支付通道编码
     * @return 支付同步策略类
     */
    public AbsPaySyncStrategy create(String channelCode) {
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
