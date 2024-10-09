package org.dromara.daxpay.channel.union.strategy;

import lombok.RequiredArgsConstructor;
import org.dromara.daxpay.channel.union.sdk.api.UnionPayKit;
import org.dromara.daxpay.channel.union.service.config.UnionPayConfigService;
import org.dromara.daxpay.channel.union.service.sync.UnionPaySyncService;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.service.bo.sync.PaySyncResultBo;
import org.dromara.daxpay.service.strategy.AbsSyncPayOrderStrategy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 云闪付支付同步接口
 * @author xxm
 * @since 2024/3/7
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class UnionSyncPayStrategy extends AbsSyncPayOrderStrategy {

    private final UnionPayConfigService unionPayConfigService;

    private final UnionPaySyncService unionPaySyncService;

    @Override
    public PaySyncResultBo doSync() {
        UnionPayKit unionPayKit = unionPayConfigService.initPayKit();
        return unionPaySyncService.sync(this.getOrder(),unionPayKit);
    }

    @Override
    public String getChannel() {
        return ChannelEnum.UNION_PAY.getCode();
    }
}
