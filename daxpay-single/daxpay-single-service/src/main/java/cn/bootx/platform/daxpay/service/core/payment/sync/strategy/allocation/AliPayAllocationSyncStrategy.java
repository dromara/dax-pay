package cn.bootx.platform.daxpay.service.core.payment.sync.strategy.allocation;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.service.core.channel.alipay.entity.AliPayConfig;
import cn.bootx.platform.daxpay.service.core.channel.alipay.service.AliPayAllocationService;
import cn.bootx.platform.daxpay.service.core.channel.alipay.service.AliPayConfigService;
import cn.bootx.platform.daxpay.service.func.AbsAllocationSyncStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 支付宝分账状态同步策略
 * @author xxm
 * @since 2024/4/12
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AliPayAllocationSyncStrategy extends AbsAllocationSyncStrategy {
    private final AliPayConfigService aliPayConfigService;

    private final AliPayAllocationService aliPayAllocationService;
    /**
     * 策略标识
     */
    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.ALI;
    }

    /**
     * 同步状态
     */
    @Override
    public void doSyncStatus() {
        AliPayConfig aliPayConfig = aliPayConfigService.getAndCheckConfig();
        aliPayConfigService.initConfig(aliPayConfig);
        aliPayAllocationService.queryStatus(this.getAllocationOrder());
    }

}
