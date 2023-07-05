package cn.bootx.platform.daxpay.core.refund.strategy;

import cn.bootx.platform.daxpay.code.pay.PayChannelEnum;
import cn.bootx.platform.daxpay.core.channel.alipay.service.AliPayCancelService;
import cn.bootx.platform.daxpay.core.channel.alipay.service.AliPaymentService;
import cn.bootx.platform.daxpay.core.channel.alipay.service.AlipayConfigService;
import cn.bootx.platform.daxpay.core.payment.service.PaymentService;
import cn.bootx.platform.daxpay.core.refund.func.AbsPayRefundStrategy;
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
    private final AliPaymentService aliPaymentService;
    private final AliPayCancelService aliPayCancelService;
    private final PaymentService paymentService;

    /**
     * 策略标识
     *
     * @see PayChannelEnum
     */
    @Override
    public PayChannelEnum getType() {
        return PayChannelEnum.ALI;
    }


    /**
     * 退款前前操作
     */
    @Override
    public void doBeforeRefundHandler() {
        alipayConfigService.initApiConfigByMchAppCode(this.getPayment().getMchAppCode());
    }

    /**
     * 退款
     */
    @Override
    public void doRefundHandler() {
        aliPayCancelService.refund(this.getPayment(), this.getRefundModeParam().getAmount());
        aliPaymentService.updatePayRefund(this.getPayment().getId(), this.getRefundModeParam().getAmount());
        paymentService.updateRefundSuccess(this.getPayment(), this.getRefundModeParam().getAmount(), PayChannelEnum.ALI);
    }


}
