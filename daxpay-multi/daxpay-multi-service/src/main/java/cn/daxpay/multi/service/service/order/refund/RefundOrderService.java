package cn.daxpay.multi.service.service.order.refund;

import cn.bootx.platform.common.redis.delay.annotation.DelayEventListener;
import cn.bootx.platform.common.redis.delay.annotation.DelayJobEvent;
import cn.bootx.platform.common.redis.delay.service.DelayJobService;
import cn.daxpay.multi.core.enums.RefundStatusEnum;
import cn.daxpay.multi.service.code.DaxPayCode;
import cn.daxpay.multi.service.dao.order.refund.RefundOrderManager;
import cn.daxpay.multi.service.entity.order.refund.RefundOrder;
import cn.daxpay.multi.service.service.trade.refund.RefundSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 退款
 *
 * @author xxm
 * @since 2022/3/2
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RefundOrderService {

    private final RefundSyncService refundSyncService;

    private final DelayJobService delayJobService;

    private final RefundOrderManager refundOrderManager;

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
    public void refundDelaySync(DelayJobEvent<Long> event) {
        var orderOpt = refundOrderManager.findById(event.getMessage());
        if (orderOpt.isPresent()) {
            var order = orderOpt.get();
            // 不是退款中不需要进行同步
            if (order.getStatus().equals(RefundStatusEnum.PROGRESS.getCode())) {
                refundSyncService.syncRefundOrder(order);
            }
        }
    }

}
