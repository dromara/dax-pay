package org.dromara.daxpay.channel.alipay.strategy.isv;

import org.dromara.daxpay.channel.alipay.entity.AliPayConfig;
import org.dromara.daxpay.channel.alipay.service.config.AlipayConfigService;
import org.dromara.daxpay.channel.alipay.service.refund.AlipayRefundService;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.service.bo.trade.RefundResultBo;
import org.dromara.daxpay.service.strategy.AbsRefundStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 支付宝退款
 * @author xxm
 * @since 2023/7/4
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AlipayIsvRefundStrategy extends AbsRefundStrategy {

    private final AlipayRefundService aliRefundService;
    private final AlipayConfigService aliPayConfigService;

    /**
     * 策略标识
     * @see ChannelEnum
     */
     @Override
    public String getChannel() {
        return ChannelEnum.ALIPAY_ISV.getCode();
    }


    /**
     * 退款
     */
    @Override
    public RefundResultBo doRefundHandler() {
        AliPayConfig aliPayConfig = aliPayConfigService.getAliPayConfig(true);
        return aliRefundService.refund(this.getRefundOrder(), aliPayConfig);
    }
}
