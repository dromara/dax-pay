package cn.bootx.platform.daxpay.core.pay.strategy;

import cn.bootx.platform.daxpay.code.pay.PayChannelEnum;
import cn.bootx.platform.daxpay.core.pay.func.AbsPayStrategy;
import cn.bootx.platform.daxpay.core.payment.service.PaymentService;
import cn.bootx.platform.daxpay.core.channel.voucher.entity.Voucher;
import cn.bootx.platform.daxpay.core.channel.voucher.service.VoucherPayService;
import cn.bootx.platform.daxpay.core.channel.voucher.service.VoucherPaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 储值卡支付
 *
 * @author xxm
 * @date 2022/3/13
 */
@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Service
@RequiredArgsConstructor
public class VoucherStrategy extends AbsPayStrategy {

    private final VoucherPayService voucherPayService;

    private final VoucherPaymentService voucherPaymentService;

    private final PaymentService paymentService;

    private List<Voucher> vouchers;

    @Override
    public PayChannelEnum getType() {
        return PayChannelEnum.VOUCHER;
    }

    /**
     * 支付前处理
     */
    @Override
    public void doBeforePayHandler() {
        // 获取并校验余额
        this.vouchers = voucherPayService.getAndCheckVoucher(this.getPayWayParam());
    }

    /**
     * 支付操作
     */
    @Override
    public void doPayHandler() {
        voucherPayService.pay(getPayWayParam().getAmount(), this.getPayment(), this.vouchers);
        voucherPaymentService.savePayment(getPayment(), getPayParam(), getPayWayParam(), vouchers);
    }

    /**
     * 成功
     */
    @Override
    public void doSuccessHandler() {
        voucherPaymentService.updateSuccess(this.getPayment().getId());
    }

    /**
     * 关闭支付
     */
    @Override
    public void doCloseHandler() {
        voucherPayService.close(this.getPayment().getId());
        voucherPaymentService.updateClose(this.getPayment().getId());
    }

    /**
     * 退款
     */
    @Override
    public void doRefundHandler() {
        voucherPayService.refund(this.getPayment().getId(), this.getPayWayParam().getAmount());
        voucherPaymentService.updateRefund(this.getPayment().getId(), this.getPayWayParam().getAmount());
        paymentService.updateRefundSuccess(this.getPayment(), this.getPayWayParam().getAmount(),
                PayChannelEnum.VOUCHER);
    }

}
