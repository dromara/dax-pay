package org.dromara.daxpay.channel.union.strategy;

import lombok.RequiredArgsConstructor;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.service.bo.sync.RefundSyncResultBo;
import org.dromara.daxpay.service.strategy.AbsSyncRefundOrderStrategy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 云闪付退款同步
 * @author xxm
 * @since 2024/3/11
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class UnionSyncRefundStrategy extends AbsSyncRefundOrderStrategy {
    @Override
    public RefundSyncResultBo doSync() {
        return null;
    }

    @Override
    public String getChannel() {
        return ChannelEnum.UNION_PAY.getCode();
    }
}
