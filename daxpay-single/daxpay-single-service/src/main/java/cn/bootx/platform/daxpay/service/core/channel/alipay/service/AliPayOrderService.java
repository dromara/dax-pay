package cn.bootx.platform.daxpay.service.core.channel.alipay.service;

import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.channel.alipay.dao.AliPayOrderManager;
import cn.bootx.platform.daxpay.service.core.channel.alipay.entity.AliPayOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.service.PayChannelOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 支付宝支付订单
 * 1.创建: 支付调起并支付成功后才会创建
 * 2.撤销: 关闭本地支付记录
 * 3.退款: 发起退款时记录
 *
 * @author xxm
 * @since 2021/2/26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AliPayOrderService {

    private final AliPayOrderManager aliPayOrderManager;

    private final PayChannelOrderService payChannelOrderService;


    /**
     * 更新异步支付记录成功状态, 并创建支付宝支付记录
     */
    @Deprecated
    public void updateAsyncSuccess(PayOrder payOrder, Integer amount) {
        // 创建支付宝支付订单
        this.createAliPayOrder(payOrder,amount);
    }

    /**
     * 创建支付宝支付订单(支付成功后才会创建)
     */
    @Deprecated
    private void createAliPayOrder(PayOrder payOrder, Integer amount) {
        String tradeNo = PaymentContextLocal.get()
                .getAsyncPayInfo()
                .getGatewayOrderNo();

        AliPayOrder aliPayOrder = new AliPayOrder();
        aliPayOrder.setTradeNo(tradeNo)
                .setPayWay(PaymentContextLocal.get().getAsyncPayInfo().getPayWay())
                .setPaymentId(payOrder.getId())
                .setAmount(amount)
                .setRefundableBalance(amount)
                .setBusinessNo(payOrder.getBusinessNo())
                .setStatus(PayStatusEnum.SUCCESS.getCode())
                // 读取回调中的支付时间
                .setPayTime(LocalDateTime.now());
        aliPayOrderManager.save(aliPayOrder);
    }

    /**
     * 取消状态, 按正常来说不会出现支付宝订单需要取消的情况
     */
    public void updateClose(Long paymentId) {
        Optional<AliPayOrder> aliPaymentOptional = aliPayOrderManager.findByPaymentId(paymentId);
        aliPaymentOptional.ifPresent(aliPayOrder -> {
            aliPayOrder.setStatus(PayStatusEnum.CLOSE.getCode());
            aliPayOrderManager.updateById(aliPayOrder);
        });
    }

    /**
     * 更新退款. 分为部分退款和全部退款
     */
    public void updateRefund(Long paymentId, int amount) {
        Optional<AliPayOrder> aliPaymentOptional = aliPayOrderManager.findByPaymentId(paymentId);
        aliPaymentOptional.ifPresent(payment -> {
            int refundableBalance = payment.getRefundableBalance() - amount;
            payment.setRefundableBalance(refundableBalance);
            // 退款完毕
            if (refundableBalance == 0) {
                payment.setStatus(PayStatusEnum.REFUNDED.getCode());
            }
            // 部分退款
            else {
                payment.setStatus(PayStatusEnum.PARTIAL_REFUND.getCode());
            }
            aliPayOrderManager.updateById(payment);
        });
    }
}
