package cn.daxpay.multi.gateway.event;

import cn.bootx.platform.common.redis.delay.annotation.DelayEventListener;
import cn.bootx.platform.common.redis.delay.annotation.DelayJobEvent;
import cn.daxpay.multi.core.enums.PayStatusEnum;
import cn.daxpay.multi.core.enums.RefundStatusEnum;
import cn.daxpay.multi.core.enums.TransferStatusEnum;
import cn.daxpay.multi.service.code.DaxPayCode;
import cn.daxpay.multi.service.dao.order.pay.PayOrderManager;
import cn.daxpay.multi.service.dao.order.refund.RefundOrderManager;
import cn.daxpay.multi.service.dao.order.transfer.TransferOrderManager;
import cn.daxpay.multi.service.entity.order.pay.PayOrder;
import cn.daxpay.multi.service.service.assist.PaymentAssistService;
import cn.daxpay.multi.service.service.trade.pay.PaySyncService;
import cn.daxpay.multi.service.service.trade.refund.RefundSyncService;
import cn.daxpay.multi.service.service.trade.transfer.TransferSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 订单相关的延时事件
 * @author xxm
 * @since 2024/8/16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderEventService {
    private final PaymentAssistService paymentAssistService;

    private final PayOrderManager payOrderManager;

    private final PaySyncService paySyncService;

    private final RefundSyncService refundSyncService;

    private final RefundOrderManager refundOrderManager;
    private final TransferOrderManager transferOrderManager;
    private final TransferSyncService transferSyncService;

    /**
     * 接收订单超时事件, 发起同步
     */
    @DelayEventListener(DaxPayCode.Event.MERCHANT_PAY_TIMEOUT)
    public void payExpired(DelayJobEvent<Long> event) {
        Optional<PayOrder> orderOpt = payOrderManager.findByIdNotTenant(event.getMessage());
        if (orderOpt.isPresent()) {
            PayOrder payOrder = orderOpt.get();
            // 不是支付中不需要进行同步
            if (payOrder.getStatus().equals(PayStatusEnum.PROGRESS.getCode())|| payOrder.getStatus().equals(PayStatusEnum.TIMEOUT.getCode())) {
                paymentAssistService.initMchAndApp(payOrder.getMchNo(),payOrder.getAppId());
                paySyncService.syncPayOrder(payOrder);
            }
        }
    }
    /**
     * 接收退款订单同步事件
     */
    @DelayEventListener(DaxPayCode.Event.MERCHANT_REFUND_SYNC)
    public void refundDelaySync(DelayJobEvent<Long> event) {
        var orderOpt = refundOrderManager.findByIdNotTenant(event.getMessage());
        if (orderOpt.isPresent()) {
            var order = orderOpt.get();
            // 不是退款中不需要进行同步
            if (order.getStatus().equals(RefundStatusEnum.PROGRESS.getCode())) {
                paymentAssistService.initMchAndApp(order.getMchNo(), order.getAppId());
                refundSyncService.syncRefundOrder(order);
            }
        }
    }


    /**
     * 接收转账订单超时事件
     */
    @DelayEventListener(DaxPayCode.Event.MERCHANT_TRANSFER_SYNC)
    public void TransferDelaySync(DelayJobEvent<Long> event) {
        var orderOpt = transferOrderManager.findByIdNotTenant(event.getMessage());
        if (orderOpt.isPresent()) {
            var order = orderOpt.get();
            // 不是退款中不需要进行同步
            if (order.getStatus().equals(TransferStatusEnum.PROGRESS.getCode())) {
                paymentAssistService.initMchAndApp(order.getMchNo(), order.getAppId());
                transferSyncService.syncTransferOrder(order);
            }
        }
    }

}
