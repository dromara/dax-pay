package org.dromara.daxpay.service.event;

import cn.bootx.platform.starter.redis.delay.annotation.DelayEventListener;
import cn.bootx.platform.starter.redis.delay.annotation.DelayJobEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.service.code.DaxPayCode;
import org.dromara.daxpay.service.service.allocation.AllocationService;
import org.dromara.daxpay.service.service.allocation.AllocationSyncService;
import org.dromara.daxpay.service.service.order.pay.PayOrderService;
import org.springframework.stereotype.Service;

/**
 * 分账事件
 * @author xxm
 * @since 2024/12/17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AllocationEventService {
    private final AllocationSyncService syncService;

    private final AllocationService allocationService;

    private final PayOrderService payOrderService;

    /**
     * 自动分账 参数为订单号
     */
    @DelayEventListener(DaxPayCode.Event.ORDER_ALLOC_START)
    public void start(DelayJobEvent<Long> event) {
        payOrderService.autoAllocation(event.getMessage());
        log.info("分账开始，订单号：{}", event.getMessage());
    }

    /**
     * 分账同步
     */
    @DelayEventListener(DaxPayCode.Event.ORDER_ALLOC_SYNC)
    public void sync(DelayJobEvent<Long> event) {
        syncService.sync(event.getMessage());
        log.info("分账同步，订单号：{}", event.getMessage());
    }

    /**
     * 分账完结
     */
    @DelayEventListener(DaxPayCode.Event.ORDER_ALLOC_FINISH)
    public void finish(DelayJobEvent<Long> event) {
        allocationService.autoFinish(event.getMessage());
        log.info("分账完结，订单号：{}", event.getMessage());
    }
}
