package cn.daxpay.single.service.core.payment.sync.strategy.allocation;

import cn.daxpay.single.core.code.PayChannelEnum;
import cn.daxpay.single.service.core.channel.alipay.entity.AliPayConfig;
import cn.daxpay.single.service.core.channel.alipay.service.AliPayAllocService;
import cn.daxpay.single.service.core.channel.alipay.service.AliPayConfigService;
import cn.daxpay.single.service.core.payment.sync.result.AllocRemoteSyncResult;
import cn.daxpay.single.service.func.AbsAllocSyncStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 支付宝分账同步策略
 * @author xxm
 * @since 2024/7/16
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AliAllocStrategy extends AbsAllocSyncStrategy {
    private final AliPayAllocService aliPayAllocService;
    private final AliPayConfigService alipayConfigService;


    @Override
    public String getChannel() {
        return PayChannelEnum.ALI.getCode();
    }

    @Override
    public AllocRemoteSyncResult doSync() {
        AliPayConfig config = alipayConfigService.getConfig();
        return aliPayAllocService.sync(this.getAllocOrder(),this.getAllocOrderDetails(), config);
    }

}
