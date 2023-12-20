package cn.bootx.platform.daxpay.core.channel.wallet.service;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.core.channel.wallet.dao.WalletPaymentManager;
import cn.bootx.platform.daxpay.core.channel.wallet.entity.Wallet;
import cn.bootx.platform.daxpay.core.channel.wallet.entity.WalletPayment;
import cn.bootx.platform.daxpay.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.param.pay.PayParam;
import cn.bootx.platform.daxpay.param.pay.PayWayParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 钱包交易记录的相关操作
 *
 * @author xxm
 * @since 2020/12/8
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WalletPaymentService {

    private final WalletPaymentManager walletPaymentManager;

    /**
     * 保存钱包支付记录
     */
    public void savePayment(PayOrder payOrder, PayParam payParam, PayWayParam payMode, Wallet wallet) {
        WalletPayment walletPayment = new WalletPayment().setWalletId(wallet.getId());
        walletPayment.setPaymentId(payOrder.getId())
            .setBusinessNo(payParam.getBusinessNo())
            .setAmount(payMode.getAmount())
            .setRefundableBalance(payMode.getAmount())
            .setStatus(payOrder.getStatus());
        walletPaymentManager.save(walletPayment);
    }

    /**
     * 更新成功状态
     */
    public void updateSuccess(Long paymentId) {
        Optional<WalletPayment> payment = walletPaymentManager.findByPaymentId(paymentId);
        if (payment.isPresent()) {
            WalletPayment walletPayment = payment.get();
            walletPayment.setStatus(PayStatusEnum.SUCCESS.getCode()).setPayTime(LocalDateTime.now());
            walletPaymentManager.updateById(walletPayment);
        }
    }

    /**
     * 关闭操作
     */
    public void updateClose(Long paymentId) {
        WalletPayment walletPayment = walletPaymentManager.findByPaymentId(paymentId)
            .orElseThrow(() -> new BizException("未查询到查询交易记录"));
        walletPayment.setStatus(PayStatusEnum.CLOSE.getCode());
        walletPaymentManager.updateById(walletPayment);
    }

    /**
     * 更新退款
     */
    public void updateRefund(Long paymentId, int amount) {
        Optional<WalletPayment> walletPayment = walletPaymentManager.findByPaymentId(paymentId);
        walletPayment.ifPresent(payment -> {
            int refundableBalance = payment.getRefundableBalance() - amount;
            payment.setRefundableBalance(refundableBalance);
            // 退款完成
            if (refundableBalance == 0) {
                payment.setStatus(PayStatusEnum.REFUNDED.getCode());
            }
            // 部分退款
            else {
                payment.setStatus(PayStatusEnum.PARTIAL_REFUND.getCode());
            }
            walletPaymentManager.updateById(payment);
        });
    }

}
