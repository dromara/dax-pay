package org.dromara.daxpay.channel.alipay.strategy.merchant;

import org.dromara.daxpay.channel.alipay.entity.AliPayConfig;
import org.dromara.daxpay.channel.alipay.service.config.AlipayConfigService;
import org.dromara.daxpay.channel.alipay.service.sync.AlipayTransferSyncService;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.service.bo.sync.TransferSyncResultBo;
import org.dromara.daxpay.service.strategy.AbsSyncTransferOrderStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 支付宝退款订单查询
 * @author xxm
 * @since 2024/7/25
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AliPaySyncTransferStrategy extends AbsSyncTransferOrderStrategy {

    private final AlipayTransferSyncService syncTransferOrderStrategy;
    private final AlipayConfigService aliPayConfigService;

    /**
     * 策略标识, 可以自行进行扩展
     *
     * @see ChannelEnum
     */
    @Override
    public String getChannel() {
        return ChannelEnum.ALIPAY.getCode();
    }

    /**
     * 异步支付单与支付网关进行状态比对后的结果
     */
    @Override
    public TransferSyncResultBo doSync() {
        AliPayConfig aliPayConfig = aliPayConfigService.getAliPayConfig(false);
        return syncTransferOrderStrategy.syncTransferStatus(this.getTransferOrder(),aliPayConfig);
    }

}
