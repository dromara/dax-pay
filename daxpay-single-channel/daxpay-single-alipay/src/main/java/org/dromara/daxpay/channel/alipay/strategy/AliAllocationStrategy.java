package org.dromara.daxpay.channel.alipay.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.channel.alipay.service.allocation.AliPayAllocationService;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.service.bo.allocation.AllocStartResultBo;
import org.dromara.daxpay.service.strategy.AbsAllocationStrategy;
import org.springframework.stereotype.Component;

/**
 * 支付宝分账
 * @author xxm
 * @since 2024/12/9
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AliAllocationStrategy extends AbsAllocationStrategy {

    private final AliPayAllocationService aliPayAllocationService;

    /**
     * 分账通道
     */
    @Override
    public String getChannel() {
        return ChannelEnum.ALI.getCode();
    }

    /**
     * 开始分账
     */
    @Override
    public AllocStartResultBo start() {
        return aliPayAllocationService.start(getOrder(), getDetails());
    }

    /**
     * 分账完结
     */
    @Override
    public void finish() {
        aliPayAllocationService.finish(getOrder(), getDetails());
    }
}
