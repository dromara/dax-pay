package cn.bootx.platform.daxpay.service.core.payment.sync.strategy.Refund;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PaySyncStatusEnum;
import cn.bootx.platform.daxpay.service.core.channel.alipay.entity.AliPayConfig;
import cn.bootx.platform.daxpay.service.core.channel.alipay.service.AliPayConfigService;
import cn.bootx.platform.daxpay.service.core.channel.alipay.service.AliPaySyncService;
import cn.bootx.platform.daxpay.service.core.payment.sync.result.RefundSyncResult;
import cn.bootx.platform.daxpay.service.func.AbsRefundSyncStrategy;
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
    public PayChannelEnum getChannel() {
        return PayChannelEnum.ALI;
    }

    /**
     * 异步支付单与支付网关进行状态比对后的结果
     *
     * @see PaySyncStatusEnum
     */
    @Override
    public RefundSyncResult doSyncStatus() {
        AliPayConfig config = alipayConfigService.getConfig();
        alipayConfigService.initConfig(config);
        return aliPaySyncService.syncRefundStatus(this.getRefundOrder());
    }
}
