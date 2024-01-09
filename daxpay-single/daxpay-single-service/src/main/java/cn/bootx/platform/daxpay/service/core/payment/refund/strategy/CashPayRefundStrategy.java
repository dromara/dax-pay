package cn.bootx.platform.daxpay.service.core.payment.refund.strategy;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.service.core.channel.cash.service.CashService;
import cn.bootx.platform.daxpay.service.core.order.pay.service.PayOrderService;
import cn.bootx.platform.daxpay.service.func.AbsPayRefundStrategy;
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
    private final PayOrderService paymentService;

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
        cashService.refund(this.getOrder().getId(), this.getChannelParam().getAmount());
        paymentService.updateRefundSuccess(this.getOrder(), this.getChannelParam().getAmount(), PayChannelEnum.CASH);
    }
}
