package org.dromara.daxpay.service.event;

import cn.bootx.platform.starter.redis.delay.annotation.DelayEventListener;
import cn.bootx.platform.starter.redis.delay.annotation.DelayJobEvent;
import org.dromara.daxpay.service.code.DaxPayCode;
import org.dromara.daxpay.service.dao.allocation.order.AllocOrderManager;
import org.dromara.daxpay.service.dao.order.pay.PayOrderManager;
import org.dromara.daxpay.service.entity.allocation.order.AllocOrder;
import org.dromara.daxpay.service.service.allocation.AllocationService;
import org.dromara.daxpay.service.service.allocation.AllocationSyncService;
import org.dromara.daxpay.service.service.assist.PaymentAssistService;
import org.dromara.daxpay.service.service.order.pay.PayOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    private final PaymentAssistService paymentAssistService;
    private final PayOrderManager payOrderManager;
    private final AllocOrderManager allocOrderManager;

    /**
     * 自动分账 参数为订单号
     */
    @DelayEventListener(DaxPayCode.Event.ORDER_ALLOC_START)
    public void start(DelayJobEvent<Long> event) {
        var payOrderOpt = payOrderManager.findByIdNotTenant(event.getMessage());
        if (payOrderOpt.isPresent()){
            var payOrder = payOrderOpt.get();
            paymentAssistService.initMchAndApp(payOrder.getAppId());
            payOrderService.autoAllocation(payOrder);
            log.info("分账开始，订单号：{}", event.getMessage());
        } else {
            log.warn("自动分账订单不存在，订单号：{}", event.getMessage());
        }
    }

    /**
     * 分账同步
     */
    @DelayEventListener(DaxPayCode.Event.ORDER_ALLOC_SYNC)
    public void sync(DelayJobEvent<Long> event) {
        Optional<AllocOrder> opt = allocOrderManager.findByIdNotTenant(event.getMessage());
        if (opt.isPresent()){
            var allocOrder = opt.get();
            paymentAssistService.initMchAndApp(allocOrder.getAppId());
            syncService.autoSync(allocOrder);
            log.info("分账同步，订单号：{}", event.getMessage());
        } else {
            log.warn("分账同步订单不存在，订单号：{}", event.getMessage());
        }
    }

    /**
     * 分账完结
     */
    @DelayEventListener(DaxPayCode.Event.ORDER_ALLOC_FINISH)
    public void finish(DelayJobEvent<Long> event) {
        Optional<AllocOrder> opt = allocOrderManager.findByIdNotTenant(event.getMessage());
        if (opt.isPresent()){
            var allocOrder = opt.get();
            paymentAssistService.initMchAndApp(allocOrder.getAppId());
            allocationService.autoFinish(allocOrder);
            log.info("分账完结，订单号：{}", event.getMessage());
        } else {
            log.warn("分账完结订单不存在，订单号：{}", event.getMessage());
        }

    }
}
