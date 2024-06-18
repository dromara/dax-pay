package cn.daxpay.single.service.core.payment.sync.strategy.Refund;

import cn.daxpay.single.core.code.PayChannelEnum;
import cn.daxpay.single.core.code.PaySyncStatusEnum;
import cn.daxpay.single.service.core.channel.union.entity.UnionPayConfig;
import cn.daxpay.single.service.core.channel.union.service.UnionPayConfigService;
import cn.daxpay.single.service.core.channel.union.service.UnionPaySyncService;
import cn.daxpay.single.service.core.payment.sync.result.RefundRemoteSyncResult;
import cn.daxpay.single.service.func.AbsRefundSyncStrategy;
import cn.daxpay.single.service.sdk.union.api.UnionPayKit;
import lombok.RequiredArgsConstructor;
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
public class UnionRefundSyncStrategy extends AbsRefundSyncStrategy {

    private final UnionPaySyncService unionPaySyncService;

    private final UnionPayConfigService unionPayConfigService;
    /**
     * 异步支付单与支付网关进行状态比对后的结果
     *
     * @see PaySyncStatusEnum
     */
    @Override
    public RefundRemoteSyncResult doSyncStatus() {
        UnionPayConfig config = unionPayConfigService.getConfig();
        UnionPayKit unionPayKit = unionPayConfigService.initPayService(config);
        return unionPaySyncService.syncRefundStatus(this.getRefundOrder(),unionPayKit);
    }

    /**
     * 策略标识
     */
    @Override
    public String getChannel() {
        return PayChannelEnum.UNION_PAY.getCode();
    }
}
