package cn.daxpay.multi.service.service.order.transfer;

import cn.bootx.platform.common.redis.delay.annotation.DelayEventListener;
import cn.bootx.platform.common.redis.delay.annotation.DelayJobEvent;
import cn.bootx.platform.common.redis.delay.service.DelayJobService;
import cn.daxpay.multi.core.enums.TransferStatusEnum;
import cn.daxpay.multi.service.code.DaxPayCode;
import cn.daxpay.multi.service.dao.order.transfer.TransferOrderManager;
import cn.daxpay.multi.service.entity.order.refund.RefundOrder;
import cn.daxpay.multi.service.service.assist.PaymentAssistService;
import cn.daxpay.multi.service.service.trade.transfer.TransferSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 转账订单服务
 * @author xxm
 * @since 2024/6/17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransferOrderService {


    private final TransferSyncService transferSyncService;

    private final DelayJobService delayJobService;

    private final TransferOrderManager transferOrderManager;
    private final PaymentAssistService paymentAssistService;

    /**
     * 注册一个两分钟后的延时同步任务
     */
    public void register(RefundOrder refundOrder) {
        delayJobService.registerByTransaction(refundOrder.getId(), DaxPayCode.Event.MERCHANT_PAY_TIMEOUT, 2*60*1000L);
    }

    /**
     * 接收订单超时事件
     */
    @DelayEventListener(DaxPayCode.Event.MERCHANT_PAY_TIMEOUT)
    public void TransferDelaySync(DelayJobEvent<Long> event) {
        var orderOpt = transferOrderManager.findById(event.getMessage());
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
