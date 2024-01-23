package cn.bootx.platform.daxpay.service.core.payment.refund.strategy;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.service.core.channel.voucher.service.VoucherPayService;
import cn.bootx.platform.daxpay.service.core.channel.voucher.service.VoucherPayOrderService;
import cn.bootx.platform.daxpay.service.core.order.pay.service.PayOrderService;
import cn.bootx.platform.daxpay.service.func.AbsPayRefundStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 储值卡支付退款
 * @author xxm
 * @since 2023/7/5
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class VoucherPayRefundStrategy extends AbsPayRefundStrategy {
    private final VoucherPayService voucherPayService;
    private final VoucherPayOrderService voucherPayOrderService;
    private final PayOrderService payOrderService;

    /**
     * 策略标识
     *
     * @see PayChannelEnum
     */
    @Override
    public PayChannelEnum getType() {
        return PayChannelEnum.VOUCHER;
    }

    /**
     * 退款
     */
    @Override
    public void doRefundHandler() {
        voucherPayService.refund(this.getPayOrder().getId(), this.getRefundChannelParam().getAmount());
        voucherPayOrderService.updateRefund(this.getPayOrder().getId(), this.getRefundChannelParam().getAmount());
        payOrderService.updateRefundSuccess(this.getPayOrder(), this.getRefundChannelParam().getAmount(), PayChannelEnum.VOUCHER);
    }

}
