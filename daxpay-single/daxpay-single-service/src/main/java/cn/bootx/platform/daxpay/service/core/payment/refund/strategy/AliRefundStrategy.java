package cn.bootx.platform.daxpay.service.core.payment.refund.strategy;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.service.core.channel.alipay.entity.AliPayConfig;
import cn.bootx.platform.daxpay.service.core.channel.alipay.service.AliPayConfigService;
import cn.bootx.platform.daxpay.service.core.channel.alipay.service.AliPayRefundService;
import cn.bootx.platform.daxpay.service.func.AbsRefundStrategy;
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
public class AliRefundStrategy extends AbsRefundStrategy {

    private final AliPayConfigService alipayConfigService;
    private final AliPayRefundService aliRefundService;
    /**
     * 策略标识
     *
     * @see PayChannelEnum
     */
    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.ALI;
    }


    /**
     * 退款前前操作
     */
    @Override
    public void doBeforeRefundHandler() {
        AliPayConfig config = alipayConfigService.getAndCheckConfig();
        alipayConfigService.initConfig(config);
    }

    /**
     * 退款
     */
    @Override
    public void doRefundHandler() {
        aliRefundService.refund(this.getRefundOrder());
    }
}
