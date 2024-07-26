package cn.daxpay.multi.channel.alipay.strategy;

import cn.daxpay.multi.channel.alipay.entity.config.AliPayConfig;
import cn.daxpay.multi.channel.alipay.service.config.AliPayConfigService;
import cn.daxpay.multi.channel.alipay.service.sync.AliPayTransferSyncService;
import cn.daxpay.multi.core.enums.ChannelEnum;
import cn.daxpay.multi.service.bo.sync.TransferSyncResultBo;
import cn.daxpay.multi.service.enums.PaySyncResultEnum;
import cn.daxpay.multi.service.strategy.AbsSyncTransferOrderStrategy;
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

    private final AliPayTransferSyncService syncTransferOrderStrategy;
    private final AliPayConfigService aliPayConfigService;

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
     *
     * @see PaySyncResultEnum
     */
    @Override
    public TransferSyncResultBo doSync() {
        AliPayConfig config = aliPayConfigService.getAliPayConfig();
        return syncTransferOrderStrategy.syncTransferStatus(getTransferOrder(),config);
    }

}
