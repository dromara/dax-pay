package cn.bootx.platform.daxpay.core.channel.wallet.service;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.util.BigDecimalUtil;
import cn.bootx.platform.daxpay.code.pay.PayStatusCode;
import cn.bootx.platform.daxpay.core.payment.entity.Payment;
import cn.bootx.platform.daxpay.core.channel.wallet.dao.WalletPaymentManager;
import cn.bootx.platform.daxpay.core.channel.wallet.entity.Wallet;
import cn.bootx.platform.daxpay.core.channel.wallet.entity.WalletPayment;
import cn.bootx.platform.daxpay.param.pay.PayWayParam;
import cn.bootx.platform.daxpay.param.pay.PayParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 钱包交易记录的相关操作
 *
 * @author xxm
 * @date 2020/12/8
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WalletPaymentService {

    private final WalletPaymentManager walletPaymentManager;

    /**
     * 保存钱包支付记录
     */
    public void savePayment(Payment payment, PayParam payParam, PayWayParam payMode, Wallet wallet) {
        WalletPayment walletPayment = new WalletPayment().setWalletId(wallet.getId());
        walletPayment.setPaymentId(payment.getId())
            .setUserId(payment.getUserId())
            .setBusinessId(payParam.getBusinessId())
            .setAmount(payMode.getAmount())
            .setRefundableBalance(payMode.getAmount())
            .setPayStatus(payment.getPayStatus());
        walletPaymentManager.save(walletPayment);
    }

    /**
     * 更新成功状态
     */
    public void updateSuccess(Long paymentId) {
        Optional<WalletPayment> payment = walletPaymentManager.findByPaymentId(paymentId);
        if (payment.isPresent()) {
            WalletPayment walletPayment = payment.get();
            walletPayment.setPayStatus(PayStatusCode.TRADE_SUCCESS).setPayTime(LocalDateTime.now());
            walletPaymentManager.updateById(walletPayment);
        }
    }

    /**
     * 关闭操作
     */
    public void updateClose(Long paymentId) {
        WalletPayment walletPayment = walletPaymentManager.findByPaymentId(paymentId)
            .orElseThrow(() -> new BizException("未查询到查询交易记录"));
        walletPayment.setPayStatus(PayStatusCode.TRADE_CANCEL);
        walletPaymentManager.updateById(walletPayment);
    }

    /**
     * 更新退款
     */
    public void updateRefund(Long paymentId, BigDecimal amount) {
        Optional<WalletPayment> walletPayment = walletPaymentManager.findByPaymentId(paymentId);
        walletPayment.ifPresent(payment -> {
            BigDecimal refundableBalance = payment.getRefundableBalance().subtract(amount);
            payment.setRefundableBalance(refundableBalance);
            if (BigDecimalUtil.compareTo(refundableBalance, BigDecimal.ZERO) == 0) {
                payment.setPayStatus(PayStatusCode.TRADE_REFUNDED);
            }
            else {
                payment.setPayStatus(PayStatusCode.TRADE_REFUNDING);
            }
            walletPaymentManager.updateById(payment);
        });
    }

}
