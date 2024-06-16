package cn.daxpay.single.service.core.payment.sync.strategy.Refund;

import cn.daxpay.single.code.PayChannelEnum;
import cn.daxpay.single.code.PaySyncStatusEnum;
import cn.daxpay.single.service.core.channel.alipay.service.AliPayConfigService;
import cn.daxpay.single.service.core.channel.alipay.service.AliPaySyncService;
import cn.daxpay.single.service.core.payment.sync.result.RefundRemoteSyncResult;
import cn.daxpay.single.service.func.AbsRefundSyncStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 支付宝退款同步策略
 * @author xxm
 * @since 2024/1/29
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AliRefundSyncStrategy extends AbsRefundSyncStrategy {

    private final AliPayConfigService alipayConfigService;

    private final AliPaySyncService aliPaySyncService;;

    /**
     * 策略标识
     */
     @Override
    public String getChannel() {
        return PayChannelEnum.ALI.getCode();
    }

    /**
     * 异步支付单与支付网关进行状态比对后的结果
     *
     * @see PaySyncStatusEnum
     */
    @Override
    public RefundRemoteSyncResult doSyncStatus() {
        return aliPaySyncService.syncRefundStatus(this.getRefundOrder());
    }
}
