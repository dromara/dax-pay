package org.dromara.daxpay.channel.alipay.strategy.merchant;

import org.dromara.daxpay.channel.alipay.entity.AliPayConfig;
import org.dromara.daxpay.channel.alipay.service.allocation.AlipayAllocationService;
import org.dromara.daxpay.channel.alipay.service.config.AlipayConfigService;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.service.bo.allocation.AllocStartResultBo;
import org.dromara.daxpay.service.bo.allocation.AllocSyncResultBo;
import org.dromara.daxpay.service.strategy.AbsAllocationStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 支付宝分账
 * @author xxm
 * @since 2024/12/9
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AlipayAllocationStrategy extends AbsAllocationStrategy {

    private final AlipayAllocationService aliPayAllocationService;

    private final AlipayConfigService aliPayConfigService;

    /**
     * 分账通道
     */
    @Override
    public String getChannel() {
        return ChannelEnum.ALIPAY.getCode();
    }

    /**
     * 开始分账
     */
    @Override
    public AllocStartResultBo start() {
        AliPayConfig aliPayConfig = aliPayConfigService.getAliPayConfig(false);
        return aliPayAllocationService.start(getOrder(), getDetails(), aliPayConfig);
    }

    /**
     * 分账完结
     */
    @Override
    public void finish() {
        AliPayConfig aliPayConfig = aliPayConfigService.getAliPayConfig(false);
        aliPayAllocationService.finish(getOrder(), getDetails(), aliPayConfig);
    }

    /**
     * 同步状态
     */
    @Override
    public AllocSyncResultBo doSync() {
        AliPayConfig aliPayConfig = aliPayConfigService.getAliPayConfig(false);
        return aliPayAllocationService.sync(getOrder(), getDetails(), aliPayConfig);
    }
}
