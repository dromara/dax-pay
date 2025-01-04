package org.dromara.daxpay.channel.alipay.strategy.merchant;

import org.dromara.daxpay.channel.alipay.entity.AliPayConfig;
import org.dromara.daxpay.channel.alipay.service.config.AlipayConfigService;
import org.dromara.daxpay.channel.alipay.service.sync.AlipaySyncService;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.service.bo.sync.PaySyncResultBo;
import org.dromara.daxpay.service.strategy.AbsSyncPayOrderStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 支付宝支付同步
 * @author xxm
 * @since 2023/7/14
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AlipaySyncOrderStrategy extends AbsSyncPayOrderStrategy {

    private final AlipaySyncService alipaySyncService;
    private final AlipayConfigService aliPayConfigService;

    /**
     * 策略标识
     */
     @Override
    public String getChannel() {
        return ChannelEnum.ALIPAY.getCode();
    }

    /**
     * 异步支付单与支付网关进行状态比对
     */
    @Override
    public PaySyncResultBo doSync() {
        AliPayConfig aliPayConfig = aliPayConfigService.getAliPayConfig(false);
        return alipaySyncService.syncPayStatus(this.getOrder(),aliPayConfig);
    }
}
