package cn.bootx.platform.daxpay.core.refund.strategy;

import cn.bootx.platform.daxpay.code.pay.PayChannelEnum;
import cn.bootx.platform.daxpay.core.channel.cash.service.CashService;
import cn.bootx.platform.daxpay.core.payment.service.PaymentService;
import cn.bootx.platform.daxpay.core.refund.func.AbsPayRefundStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 现金支付退款
 * @author xxm
 * @since 2023/7/5
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class CashPayRefundStrategy extends AbsPayRefundStrategy {

    private final CashService cashService;
    private final PaymentService paymentService;

    /**
     * 策略标识
     *
     * @see PayChannelEnum
     */
    @Override
    public PayChannelEnum getType() {
        return PayChannelEnum.CASH;
    }

    /**
     * 退款
     */
    @Override
    public void doRefundHandler() {
        cashService.refund(this.getPayment().getId(), this.getRefundModeParam().getAmount());
        paymentService.updateRefundSuccess(this.getPayment(), this.getRefundModeParam().getAmount(), PayChannelEnum.CASH);
    }
}
