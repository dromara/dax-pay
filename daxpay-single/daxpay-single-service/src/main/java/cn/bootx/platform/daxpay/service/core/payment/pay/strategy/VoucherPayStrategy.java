package cn.bootx.platform.daxpay.service.core.payment.pay.strategy;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.service.core.channel.voucher.entity.Voucher;
import cn.bootx.platform.daxpay.service.core.channel.voucher.entity.VoucherRecord;
import cn.bootx.platform.daxpay.service.core.channel.voucher.service.VoucherPayService;
import cn.bootx.platform.daxpay.service.core.channel.voucher.service.VoucherPayOrderService;
import cn.bootx.platform.daxpay.service.func.AbsPayStrategy;
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

    private final VoucherPayOrderService voucherPayOrderService;

    private Voucher voucher;

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
        this.voucher = voucherPayService.getAndCheckVoucher(this.getPayChannelParam());
    }

    /**
     * 支付操作
     */
    @Override
    public void doPayHandler() {
        VoucherRecord voucherRecord;
//        if (this.getOrder().isAsyncPay()){
//            voucherRecord = voucherPayService.freezeBalance(this.getPayChannelParam().getAmount(), this.getOrder(), this.voucher);
//        } else {
        voucherRecord = voucherPayService.pay(this.getPayChannelParam().getAmount(), this.getOrder(), this.voucher);
//        }
        voucherPayOrderService.savePayment(this.getOrder(), getPayParam(), getPayChannelParam(), voucherRecord);
    }

    /**
     * 成功
     */
    @Override
    public void doSuccessHandler() {
        if (this.getOrder().isAsyncPay()){
            voucherPayService.paySuccess(this.getOrder().getId());
        }
        this.getChannelOrder().setStatus(PayStatusEnum.SUCCESS.getCode());
        voucherPayOrderService.updateSuccess(this.getOrder().getId());
    }

}
