package cn.bootx.platform.daxpay.core.channel.cash.service;

import cn.bootx.platform.common.core.util.BigDecimalUtil;
import cn.bootx.platform.daxpay.code.pay.PayStatusCode;
import cn.bootx.platform.daxpay.core.channel.cash.dao.CashPaymentManager;
import cn.bootx.platform.daxpay.core.channel.cash.entity.CashPayOrder;
import cn.bootx.platform.daxpay.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.core.payment.entity.Payment;
import cn.bootx.platform.daxpay.param.pay.PayParam;
import cn.bootx.platform.daxpay.param.pay.PayWayParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * 现金支付
 *
 * @author xxm
 * @since 2021/6/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CashService {

    private final CashPaymentManager cashPaymentManager;

    /**
     * 支付
     */
    public void pay(PayWayParam payMode, PayOrder payment, PayParam payParam) {
        CashPayOrder walletPayment = new CashPayOrder();
        walletPayment.setPaymentId(payment.getId())
            .setBusinessNo(payParam.getBusinessNo())
            .setAmount(payMode.getAmount())
            .setRefundableBalance(payMode.getAmount())
            .setStatus(payment.getStatus());
        cashPaymentManager.save(walletPayment);
    }

    /**
     * 关闭
     */
    public void close(Long paymentId) {
        Optional<CashPayOrder> cashPaymentOpt = cashPaymentManager.findByPaymentId(paymentId);
        cashPaymentOpt.ifPresent(cashPayOrder -> {
            cashPayOrder.setStatus(PayStatusCode.TRADE_CANCEL);
            cashPaymentManager.updateById(cashPayOrder);
        });
    }

    /**
     * 退款
     */
    public void refund(Long paymentId, BigDecimal amount) {
        Optional<CashPayOrder> cashPayment = cashPaymentManager.findByPaymentId(paymentId);
        cashPayment.ifPresent(payment -> {
            BigDecimal refundableBalance = payment.getRefundableBalance().subtract(amount);
            if (BigDecimalUtil.compareTo(refundableBalance, BigDecimal.ZERO) == 0) {
                payment.setStatus(PayStatusCode.TRADE_REFUNDED);
            }
            else {
                payment.setStatus(PayStatusCode.TRADE_REFUNDING);
            }
            cashPaymentManager.updateById(payment);
        });
    }

}
