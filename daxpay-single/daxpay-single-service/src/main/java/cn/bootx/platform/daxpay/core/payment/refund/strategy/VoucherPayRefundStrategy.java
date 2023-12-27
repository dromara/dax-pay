package cn.bootx.platform.daxpay.core.payment.refund.strategy;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.core.channel.voucher.service.VoucherPayService;
import cn.bootx.platform.daxpay.core.channel.voucher.service.VoucherPaymentService;
import cn.bootx.platform.daxpay.core.order.pay.service.PayOrderService;
import cn.bootx.platform.daxpay.func.AbsPayRefundStrategy;
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
    private final VoucherPaymentService voucherPaymentService;
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
     * 退款前对处理
     */
    @Override
    public void doBeforeRefundHandler() {
    }

    /**
     * 退款
     */
    @Override
    public void doRefundHandler() {
        voucherPayService.refund(this.getOrder().getId(), this.getChannelParam().getAmount());
        voucherPaymentService.updateRefund(this.getOrder().getId(), this.getChannelParam().getAmount());
        payOrderService.updateRefundSuccess(this.getOrder(), this.getChannelParam().getAmount(), PayChannelEnum.VOUCHER);
    }
}
