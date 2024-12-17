package org.dromara.daxpay.service.event;

import cn.bootx.platform.starter.redis.delay.annotation.DelayEventListener;
import cn.bootx.platform.starter.redis.delay.annotation.DelayJobEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.service.code.DaxPayCode;
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


    /**
     * 自动分账 参数为订单号
     */
    @DelayEventListener(DaxPayCode.Event.ORDER_ALLOC_START)
    public void start(DelayJobEvent<Long> event) {
        log.info("分账开始，订单号：{}", event.getMessage());
    }

    /**
     * 分账同步
     */
    @DelayEventListener(DaxPayCode.Event.ORDER_ALLOC_SYNC)
    public void sync(DelayJobEvent<Long> event) {
        log.info("分账同步，订单号：{}", event.getMessage());
    }

    @DelayEventListener(DaxPayCode.Event.ORDER_ALLOC_FINISH)
    public void finish(DelayJobEvent<Long> event) {
        log.info("分账完成，订单号：{}", event.getMessage());
    }
}
