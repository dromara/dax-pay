package cn.bootx.platform.daxpay.service.core.payment.sync.strategy.pay;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PaySyncStatusEnum;
import cn.bootx.platform.daxpay.service.core.channel.union.entity.UnionPayConfig;
import cn.bootx.platform.daxpay.service.core.channel.union.service.UnionPayConfigService;
import cn.bootx.platform.daxpay.service.core.channel.union.service.UnionPaySyncService;
import cn.bootx.platform.daxpay.service.core.payment.sync.result.PayGatewaySyncResult;
import cn.bootx.platform.daxpay.service.func.AbsPaySyncStrategy;
import cn.bootx.platform.daxpay.service.sdk.union.api.UnionPayKit;
import lombok.RequiredArgsConstructor;
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
public class UnionPaySyncStrategy extends AbsPaySyncStrategy {

    private final UnionPaySyncService unionPaySyncService;

    private final UnionPayConfigService unionPayConfigService;

    /**
     * 异步支付单与支付网关进行状态比对后的结果
     *
     * @see PaySyncStatusEnum
     */
    @Override
    public PayGatewaySyncResult doSyncStatus() {
        UnionPayConfig config = unionPayConfigService.getConfig();
        UnionPayKit unionPayKit = unionPayConfigService.initPayService(config);
        return unionPaySyncService.syncPayStatus(this.getOrder(),unionPayKit);
    }

    /**
     * 策略标识
     */
    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.UNION_PAY;
    }
}
