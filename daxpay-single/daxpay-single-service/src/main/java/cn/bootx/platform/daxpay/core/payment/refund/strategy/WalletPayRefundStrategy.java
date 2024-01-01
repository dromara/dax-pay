package cn.bootx.platform.daxpay.core.payment.refund.strategy;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.core.channel.wallet.service.WalletPayService;
import cn.bootx.platform.daxpay.core.channel.wallet.service.WalletPayOrderService;
import cn.bootx.platform.daxpay.core.record.pay.service.PayOrderService;
import cn.bootx.platform.daxpay.func.AbsPayRefundStrategy;
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
    private final WalletPayOrderService walletPayOrderService;
    private final PayOrderService payOrderService;
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
        walletPayService.refund(this.getOrder().getId(), this.getChannelParam().getAmount());
        walletPayOrderService.updateRefund(this.getOrder().getId(), this.getChannelParam().getAmount());
        payOrderService.updateRefundSuccess(this.getOrder(), this.getChannelParam().getAmount(), PayChannelEnum.WALLET);
    }
}
