package cn.bootx.platform.daxpay.core.refund.strategy;

import cn.bootx.platform.daxpay.code.pay.PayChannelEnum;
import cn.bootx.platform.daxpay.core.channel.wallet.service.WalletPayService;
import cn.bootx.platform.daxpay.core.channel.wallet.service.WalletPaymentService;
import cn.bootx.platform.daxpay.core.payment.service.PaymentService;
import cn.bootx.platform.daxpay.core.refund.func.AbsPayRefundStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 钱包支付退款
 * @author xxm
 * @since 2023/7/5
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class WalletPayRefundStrategy extends AbsPayRefundStrategy {
    private final WalletPayService walletPayService;
    private final WalletPaymentService walletPaymentService;
    private final PaymentService paymentService;
    /**
     * 策略标识
     *
     * @see PayChannelEnum
     */
    @Override
    public PayChannelEnum getType() {
        return PayChannelEnum.WALLET;
    }

    /**
     * 退款
     */
    @Override
    public void doRefundHandler() {
        walletPayService.refund(this.getPayment().getId(), this.getRefundModeParam().getAmount());
        walletPaymentService.updateRefund(this.getPayment().getId(), this.getRefundModeParam().getAmount());
        paymentService.updateRefundSuccess(this.getPayment(), this.getRefundModeParam().getAmount(), PayChannelEnum.WALLET);
    }
}
