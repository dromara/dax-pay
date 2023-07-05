package cn.bootx.platform.daxpay.core.refund.strategy;

import cn.bootx.platform.daxpay.code.pay.PayChannelEnum;
import cn.bootx.platform.daxpay.core.channel.voucher.service.VoucherPayService;
import cn.bootx.platform.daxpay.core.channel.voucher.service.VoucherPaymentService;
import cn.bootx.platform.daxpay.core.payment.service.PaymentService;
import cn.bootx.platform.daxpay.core.refund.func.AbsPayRefundStrategy;
import cn.bootx.platform.daxpay.param.channel.voucher.VoucherRefundParam;
import cn.bootx.platform.daxpay.param.refund.RefundModeParam;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
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
    private final PaymentService paymentService;

    private VoucherRefundParam voucherRefundParam;

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
        RefundModeParam refundModeParam = this.getRefundModeParam();
        this.voucherRefundParam = new VoucherRefundParam();
        if (StrUtil.isNotBlank(refundModeParam.getExtraParamsJson())){
            this.voucherRefundParam = JSONUtil.toBean(refundModeParam.getExtraParamsJson(), VoucherRefundParam.class);
        }
    }

    /**
     * 退款
     */
    @Override
    public void doRefundHandler() {
        voucherPayService.refund(this.getPayment().getId(), this.getRefundModeParam().getAmount(),voucherRefundParam);
        voucherPaymentService.updateRefund(this.getPayment().getId(), this.getRefundModeParam().getAmount());
        paymentService.updateRefundSuccess(this.getPayment(), this.getRefundModeParam().getAmount(),
                PayChannelEnum.VOUCHER);
    }
}
