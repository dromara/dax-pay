package cn.bootx.platform.daxpay.service.core.channel.wechat.service;

import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.channel.wechat.dao.WeChatPayOrderManager;
import cn.bootx.platform.daxpay.service.core.channel.wechat.entity.WeChatPayOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 微信支付记录单
 *
 * @author xxm
 * @since 2021/6/21
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatPayOrderService {

    private final WeChatPayOrderManager weChatPayOrderManager;

    /**
     * 异步支付成功, 更新支付记录成功状态, 并创建微信支付记录
     */
    public void updateAsyncSuccess(PayOrder payOrder, int amount) {
        this.createWeChatOrder(payOrder, amount);
    }

    /**
     * 并创建微信支付记录
     */
    private void createWeChatOrder(PayOrder payOrder, int amount) {
        String tradeNo = PaymentContextLocal.get()
                .getAsyncPayInfo()
                .getGatewayOrderNo();
        // 创建微信支付记录
        WeChatPayOrder wechatPayOrder = new WeChatPayOrder();
        wechatPayOrder.setTradeNo(tradeNo)
                .setPayWay(PaymentContextLocal.get().getAsyncPayInfo().getPayWay())
                .setPaymentId(payOrder.getId())
                .setAmount(amount)
                .setRefundableBalance(amount)
                .setBusinessNo(payOrder.getBusinessNo())
                .setStatus(PayStatusEnum.SUCCESS.getCode())
                .setPayTime(LocalDateTime.now());
        weChatPayOrderManager.save(wechatPayOrder);
    }

    /**
     * 取消状态
     */
    public void updateClose(Long paymentId) {
        Optional<WeChatPayOrder> weChatPaymentOptional = weChatPayOrderManager.findByPaymentId(paymentId);
        weChatPaymentOptional.ifPresent(weChatPayment -> {
            weChatPayment.setStatus(PayStatusEnum.CLOSE.getCode());
            weChatPayOrderManager.updateById(weChatPayment);
        });
    }

    /**
     * 更新退款
     */
    public void updateRefund(Long paymentId, int amount) {
        Optional<WeChatPayOrder> weChatPayment = weChatPayOrderManager.findByPaymentId(paymentId);
        weChatPayment.ifPresent(payment -> {
            int refundableBalance = payment.getRefundableBalance() - amount;
            payment.setRefundableBalance(refundableBalance);
            if (refundableBalance == 0) {
                payment.setStatus(PayStatusEnum.REFUNDED.getCode());
            }
            else {
                payment.setStatus(PayStatusEnum.PARTIAL_REFUND.getCode());
            }
            weChatPayOrderManager.updateById(payment);
        });
    }

}
