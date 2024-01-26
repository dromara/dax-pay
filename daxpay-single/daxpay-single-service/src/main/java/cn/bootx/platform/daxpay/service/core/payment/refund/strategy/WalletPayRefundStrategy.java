package cn.bootx.platform.daxpay.service.core.payment.refund.strategy;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.service.core.channel.wallet.service.WalletPayService;
import cn.bootx.platform.daxpay.service.core.channel.wallet.service.WalletPayOrderService;
import cn.bootx.platform.daxpay.service.core.order.pay.service.PayOrderService;
import cn.bootx.platform.daxpay.service.func.AbsRefundStrategy;
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
public class WalletPayRefundStrategy extends AbsRefundStrategy {
    private final WalletPayService walletPayService;
    private final WalletPayOrderService walletPayOrderService;
    private final PayOrderService payOrderService;
    /**
     * 策略标识
     *
     * @see PayChannelEnum
     */
    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.WALLET;
    }

    /**
     * 退款
     */
    @Override
    public void doRefundHandler() {
        walletPayService.refund(this.getPayOrder().getId(), this.getRefundChannelParam().getAmount());
        walletPayOrderService.updateRefund(this.getPayOrder().getId(), this.getRefundChannelParam().getAmount());
    }
}
