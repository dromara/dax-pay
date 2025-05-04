package org.dromara.daxpay.service.event;

import cn.bootx.platform.starter.redis.delay.annotation.DelayEventListener;
import cn.bootx.platform.starter.redis.delay.annotation.DelayJobEvent;
import org.dromara.daxpay.core.enums.PayStatusEnum;
import org.dromara.daxpay.core.enums.RefundStatusEnum;
import org.dromara.daxpay.core.enums.TransferStatusEnum;
import org.dromara.daxpay.service.code.DaxPayCode;
import org.dromara.daxpay.service.dao.order.pay.PayOrderManager;
import org.dromara.daxpay.service.dao.order.refund.RefundOrderManager;
import org.dromara.daxpay.service.dao.order.transfer.TransferOrderManager;
import org.dromara.daxpay.service.entity.order.pay.PayOrder;
import org.dromara.daxpay.service.service.assist.PaymentAssistService;
import org.dromara.daxpay.service.service.trade.pay.PayCloseService;
import org.dromara.daxpay.service.service.trade.pay.PaySyncService;
import org.dromara.daxpay.service.service.trade.refund.RefundSyncService;
import org.dromara.daxpay.service.service.trade.transfer.TransferSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 订单交易相关的延时事件
 * @author xxm
 * @since 2024/8/16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TradeOrderEventService {
    private final PaymentAssistService paymentAssistService;

    private final PayOrderManager payOrderManager;

    private final PaySyncService paySyncService;

    private final RefundSyncService refundSyncService;

    private final RefundOrderManager refundOrderManager;

    private final TransferOrderManager transferOrderManager;

    private final TransferSyncService transferSyncService;
    private final PayCloseService payCloseService;

    /**
     * 接收订单超时事件, 发起同步
     */
    @DelayEventListener(DaxPayCode.Event.ORDER_PAY_TIMEOUT)
    public void payExpired(DelayJobEvent<Long> event) {
        Optional<PayOrder> orderOpt = payOrderManager.findByIdNotTenant(event.getMessage());
        if (orderOpt.isPresent()) {
            PayOrder payOrder = orderOpt.get();
            // 不是支付中不需要进行同步
            if (payOrder.getStatus().equals(PayStatusEnum.PROGRESS.getCode())) {
                paymentAssistService.initMchAndApp(payOrder.getAppId());
                paySyncService.syncPayOrder(payOrder);
            }
            // 待支付走超时关闭
            if (payOrder.getStatus().equals(PayStatusEnum.WAIT.getCode()) ) {
                paymentAssistService.initMchAndApp(payOrder.getAppId());
                payCloseService.closeOrder(payOrder,false);
            }
        }
    }
    /**
     * 接收退款订单同步事件
     */
    @DelayEventListener(DaxPayCode.Event.ORDER_REFUND_SYNC)
    public void refundDelaySync(DelayJobEvent<Long> event) {
        var orderOpt = refundOrderManager.findByIdNotTenant(event.getMessage());
        if (orderOpt.isPresent()) {
            var order = orderOpt.get();
            // 不是退款中不需要进行同步
            if (order.getStatus().equals(RefundStatusEnum.PROGRESS.getCode())) {
                paymentAssistService.initMchAndApp(order.getAppId());
                refundSyncService.syncRefundOrder(order);
            }
        }
    }

    /**
     * 接收转账订单超时事件
     */
    @DelayEventListener(DaxPayCode.Event.ORDER_TRANSFER_SYNC)
    public void TransferDelaySync(DelayJobEvent<Long> event) {
        var orderOpt = transferOrderManager.findByIdNotTenant(event.getMessage());
        if (orderOpt.isPresent()) {
            var order = orderOpt.get();
            // 不是退款中不需要进行同步
            if (order.getStatus().equals(TransferStatusEnum.PROGRESS.getCode())) {
                paymentAssistService.initMchAndApp(order.getAppId());
                transferSyncService.syncTransferOrder(order);
            }
        }
    }

}
