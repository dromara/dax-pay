package cn.daxpay.multi.channel.wechat.strategy;

import cn.daxpay.multi.core.enums.ChannelEnum;
import cn.daxpay.multi.service.bo.sync.PaySyncResultBo;
import cn.daxpay.multi.service.enums.PaySyncResultEnum;
import cn.daxpay.multi.service.strategy.AbsSyncPayOrderStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 微信支付订单同步
 * @author xxm
 * @since 2023/7/14
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class WechatSyncPayOrderStrategy extends AbsSyncPayOrderStrategy {


    /**
     * 策略标识, 可以自行进行扩展
     *
     * @see ChannelEnum
     */
    @Override
    public String getChannel() {
        return ChannelEnum.WECHAT.getCode();
    }

    /**
     * 异步支付单与支付网关进行状态比对后的结果
     *
     * @see PaySyncResultEnum
     */
    @Override
    public PaySyncResultBo doSync() {
        return null;
    }
}
