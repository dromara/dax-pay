package org.dromara.daxpay.channel.alipay.strategy;

import lombok.RequiredArgsConstructor;
import org.dromara.daxpay.channel.alipay.service.sync.AliPayTransferSyncService;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.service.bo.sync.TransferSyncResultBo;
import org.dromara.daxpay.service.strategy.AbsSyncTransferOrderStrategy;
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

    private final AliPayTransferSyncService syncTransferOrderStrategy;

    /**
     * 策略标识, 可以自行进行扩展
     *
     * @see ChannelEnum
     */
    @Override
    public String getChannel() {
        return ChannelEnum.ALI.getCode();
    }

    /**
     * 异步支付单与支付网关进行状态比对后的结果
     */
    @Override
    public TransferSyncResultBo doSync() {
        return syncTransferOrderStrategy.syncTransferStatus(getTransferOrder());
    }

}
