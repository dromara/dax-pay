package org.dromara.daxpay.channel.union.strategy;

import lombok.RequiredArgsConstructor;
import org.dromara.daxpay.channel.union.sdk.api.UnionPayKit;
import org.dromara.daxpay.channel.union.service.config.UnionPayConfigService;
import org.dromara.daxpay.channel.union.service.sync.UnionRefundSyncService;
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
    private final UnionPayConfigService unionPayConfigService;
    private final UnionRefundSyncService unionSyncRefundService;

    @Override
    public RefundSyncResultBo doSync() {
        UnionPayKit unionPayKit = unionPayConfigService.initPayKit();
        return unionSyncRefundService.sync(this.getRefundOrder(),unionPayKit);
    }

    @Override
    public String getChannel() {
        return ChannelEnum.UNION_PAY.getCode();
    }
}
