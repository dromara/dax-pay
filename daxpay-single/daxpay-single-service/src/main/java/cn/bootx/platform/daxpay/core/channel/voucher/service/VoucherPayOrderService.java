package cn.bootx.platform.daxpay.core.channel.voucher.service;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.core.channel.voucher.dao.VoucherPaymentManager;
import cn.bootx.platform.daxpay.core.channel.voucher.entity.VoucherPayOrder;
import cn.bootx.platform.daxpay.core.channel.voucher.entity.VoucherRecord;
import cn.bootx.platform.daxpay.core.record.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.param.pay.PayParam;
import cn.bootx.platform.daxpay.param.pay.PayWayParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 储值卡支付记录
 *
 * @author xxm
 * @since 2022/3/14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VoucherPayOrderService {

    private final VoucherPaymentManager voucherPaymentManager;

    /**
     * 添加支付记录
     */
    public void savePayment(PayOrder payOrder, PayParam payParam, PayWayParam payMode, VoucherRecord voucherRecord) {
        VoucherPayOrder voucherPayOrder = new VoucherPayOrder().setVoucherRecord(voucherRecord);
        voucherPayOrder.setPaymentId(payOrder.getId())
            .setBusinessNo(payParam.getBusinessNo())
            .setAmount(payMode.getAmount())
            .setRefundableBalance(payMode.getAmount())
            .setStatus(payOrder.getStatus());
        voucherPaymentManager.save(voucherPayOrder);
    }

    /**
     * 更新成功状态
     */
    public void updateSuccess(Long paymentId) {
        Optional<VoucherPayOrder> payment = voucherPaymentManager.findByPaymentId(paymentId);
        if (payment.isPresent()) {
            VoucherPayOrder voucherPayOrder = payment.get();
            voucherPayOrder.setStatus(PayStatusEnum.SUCCESS.getCode())
                    .setPayTime(LocalDateTime.now());
            voucherPaymentManager.updateById(voucherPayOrder);
        }
    }

    /**
     * 关闭操作
     */
    public void updateClose(Long paymentId) {
        VoucherPayOrder payment = voucherPaymentManager.findByPaymentId(paymentId)
            .orElseThrow(() -> new BizException("未查询到查询交易记录"));
        payment.setStatus(PayStatusEnum.CLOSE.getCode());
        voucherPaymentManager.updateById(payment);
    }

    /**
     * 更新退款
     */
    public void updateRefund(Long paymentId, int amount) {
        Optional<VoucherPayOrder> voucherPayment = voucherPaymentManager.findByPaymentId(paymentId);
        voucherPayment.ifPresent(payOrder -> {
            int refundableBalance = payOrder.getRefundableBalance() - amount;
            payOrder.setRefundableBalance(refundableBalance);
            // 全部退款
            if (refundableBalance == 0) {
                payOrder.setStatus(PayStatusEnum.REFUNDED.getCode());
            }
            // 部分退款
            else {
                payOrder.setStatus(PayStatusEnum.PARTIAL_REFUND.getCode());
            }
            voucherPaymentManager.updateById(payOrder);
        });
    }

}
