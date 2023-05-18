package cn.bootx.platform.daxpay.core.channel.wallet.service;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.daxpay.code.pay.PayStatusCode;
import cn.bootx.platform.daxpay.code.paymodel.WalletCode;
import cn.bootx.platform.daxpay.core.payment.entity.Payment;
import cn.bootx.platform.daxpay.core.channel.wallet.dao.WalletLogManager;
import cn.bootx.platform.daxpay.core.channel.wallet.dao.WalletManager;
import cn.bootx.platform.daxpay.core.channel.wallet.dao.WalletPaymentManager;
import cn.bootx.platform.daxpay.core.channel.wallet.entity.Wallet;
import cn.bootx.platform.daxpay.core.channel.wallet.entity.WalletLog;
import cn.bootx.platform.daxpay.core.channel.wallet.entity.WalletPayment;
import cn.bootx.platform.daxpay.exception.waller.WalletLackOfBalanceException;
import cn.bootx.platform.daxpay.exception.waller.WalletNotExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * 钱包支付操作
 *
 * @author xxm
 * @date 2021/2/27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WalletPayService {

    private final WalletManager walletManager;

    private final WalletPaymentManager walletPaymentManager;

    private final WalletLogManager walletLogManager;

    /**
     * 支付操作
     * @param amount 付款金额
     * @param payment 支付记录
     * @param wallet 钱包
     */
    public void pay(BigDecimal amount, Payment payment, Wallet wallet) {
        // 扣减余额
        int i = walletManager.reduceBalance(wallet.getId(), amount);

        // 判断操作结果
        if (i < 1) {
            throw new WalletLackOfBalanceException();
        }
        // 日志
        WalletLog walletLog = new WalletLog().setWalletId(wallet.getId())
            .setUserId(wallet.getUserId())
            .setPaymentId(payment.getId())
            .setAmount(amount)
            .setType(WalletCode.LOG_PAY)
            .setRemark(String.format("钱包支付金额 %.2f ", amount))
            .setOperationSource(WalletCode.OPERATION_SOURCE_USER)
            .setBusinessId(payment.getBusinessId());
        walletLogManager.save(walletLog);
    }

    /**
     * 取消支付并返还金额
     */
    public void close(Long paymentId) {
        // 钱包支付记录
        walletPaymentManager.findByPaymentId(paymentId).ifPresent(walletPayment -> {
            Optional<Wallet> walletOpt = walletManager.findById(walletPayment.getWalletId());
            if (!walletOpt.isPresent()) {
                log.error("钱包出现恶性问题,需要人工排查");
                return;
            }
            Wallet wallet = walletOpt.get();
            walletPayment.setPayStatus(PayStatusCode.TRADE_CANCEL);
            walletPaymentManager.save(walletPayment);

            // 金额返还
            walletManager.increaseBalance(wallet.getId(), walletPayment.getAmount());

            // 记录日志
            WalletLog walletLog = new WalletLog().setAmount(walletPayment.getAmount())
                .setPaymentId(walletPayment.getPaymentId())
                .setWalletId(wallet.getId())
                .setUserId(wallet.getUserId())
                .setType(WalletCode.LOG_PAY_CLOSE)
                .setRemark(String.format("取消支付返回金额 %.2f ", walletPayment.getAmount()))
                .setOperationSource(WalletCode.OPERATION_SOURCE_SYSTEM)
                .setBusinessId(walletPayment.getBusinessId());
            // save log
            walletLogManager.save(walletLog);
        });
    }

    /**
     * 退款
     */
    @Transactional(rollbackFor = Exception.class)
    public void refund(Long paymentId, BigDecimal amount) {
        // 钱包支付记录
        WalletPayment walletPayment = walletPaymentManager.findByPaymentId(paymentId)
            .orElseThrow(() -> new BizException("钱包支付记录不存在"));
        // 获取钱包
        Wallet wallet = walletManager.findById(walletPayment.getWalletId()).orElseThrow(WalletNotExistsException::new);
        walletManager.increaseBalance(wallet.getId(), amount);

        WalletLog walletLog = new WalletLog().setAmount(amount)
            .setPaymentId(walletPayment.getPaymentId())
            .setWalletId(wallet.getId())
            .setUserId(wallet.getUserId())
            .setType(WalletCode.LOG_REFUND)
            .setRemark(String.format("钱包退款金额 %.2f ", amount))
            .setOperationSource(WalletCode.OPERATION_SOURCE_ADMIN)
            .setBusinessId(walletPayment.getBusinessId());
        // save log
        walletLogManager.save(walletLog);
    }

}
