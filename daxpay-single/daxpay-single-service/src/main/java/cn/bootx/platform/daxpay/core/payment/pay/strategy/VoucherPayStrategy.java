package cn.bootx.platform.daxpay.core.payment.pay.strategy;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.core.channel.voucher.entity.Voucher;
import cn.bootx.platform.daxpay.core.channel.voucher.entity.VoucherRecord;
import cn.bootx.platform.daxpay.core.channel.voucher.service.VoucherPayService;
import cn.bootx.platform.daxpay.core.channel.voucher.service.VoucherPaymentService;
import cn.bootx.platform.daxpay.func.AbsPayStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 储值卡支付
 *
 * @author xxm
 * @since 2022/3/13
 */
@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Service
@RequiredArgsConstructor
public class VoucherPayStrategy extends AbsPayStrategy {

    private final VoucherPayService voucherPayService;

    private final VoucherPaymentService voucherPaymentService;

    private Voucher vouchers;

    @Override
    public PayChannelEnum getType() {
        return PayChannelEnum.VOUCHER;
    }

    /**
     * 支付前处理
     */
    @Override
    public void doBeforePayHandler() {
        // 获取并校验储值卡
        this.vouchers = voucherPayService.getAndCheckVoucher(this.getPayWayParam());
    }

    /**
     * 支付操作
     */
    @Override
    public void doPayHandler() {
        VoucherRecord voucherRecord;
        if (this.getOrder().isAsyncPay()){
            voucherRecord = voucherPayService.freezeBalance(this.getPayWayParam().getAmount(), this.getOrder(), this.vouchers);
        } else {
            voucherRecord = voucherPayService.pay(this.getPayWayParam().getAmount(), this.getOrder(), this.vouchers);
        }
        voucherPaymentService.savePayment(this.getOrder(), getPayParam(), getPayWayParam(), voucherRecord);
    }

    /**
     * 成功
     */
    @Override
    public void doSuccessHandler() {
        if (this.getOrder().isAsyncPay()){
            voucherPayService.paySuccess(this.getOrder().getId());
        }
        voucherPaymentService.updateSuccess(this.getOrder().getId());
    }

    /**
     * 关闭支付
     */
    @Override
    public void doCloseHandler() {
        voucherPayService.close(this.getOrder().getId());
        voucherPaymentService.updateClose(this.getOrder().getId());
    }

}
