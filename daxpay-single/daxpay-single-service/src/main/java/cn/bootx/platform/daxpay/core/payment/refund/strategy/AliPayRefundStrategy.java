package cn.bootx.platform.daxpay.core.payment.refund.strategy;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.core.channel.alipay.entity.AlipayConfig;
import cn.bootx.platform.daxpay.core.channel.alipay.service.AliPayOrderService;
import cn.bootx.platform.daxpay.core.channel.alipay.service.AliPayRefundService;
import cn.bootx.platform.daxpay.core.channel.alipay.service.AlipayConfigService;
import cn.bootx.platform.daxpay.core.order.pay.service.PayOrderService;
import cn.bootx.platform.daxpay.func.AbsPayRefundStrategy;
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
public class AliPayRefundStrategy extends AbsPayRefundStrategy {

    private final AlipayConfigService alipayConfigService;
    private final AliPayOrderService aliPayOrderService;
    private final AliPayRefundService aliRefundService;
    /**
     * 策略标识
     *
     * @see PayChannelEnum
     */
    @Override
    public PayChannelEnum getType() {
        return PayChannelEnum.ALI;
    }

    private final PayOrderService payOrderService;


    /**
     * 退款前前操作
     */
    @Override
    public void doBeforeRefundHandler() {
        AlipayConfig config = alipayConfigService.getConfig();
        alipayConfigService.initConfig(config);
    }

    /**
     * 退款
     */
    @Override
    public void doRefundHandler() {
        aliRefundService.refund(this.getOrder(), this.getChannelParam().getAmount());
        aliPayOrderService.updatePayRefund(this.getOrder().getId(), this.getChannelParam().getAmount());
        payOrderService.updateRefundSuccess(this.getOrder(), this.getChannelParam().getAmount(), PayChannelEnum.ALI);
    }


}
