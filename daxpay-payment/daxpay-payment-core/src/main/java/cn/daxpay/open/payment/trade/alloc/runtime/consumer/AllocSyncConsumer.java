package cn.daxpay.open.payment.trade.alloc.runtime.consumer;

import cn.daxpay.open.payment.trade.alloc.runtime.mq.AllocSyncMessage;
import cn.daxpay.open.payment.trade.alloc.runtime.service.AllocSyncService;
import cn.daxpay.open.payment.trade.runtime.mq.PayArtemisConstants;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/// # 分账延迟同步消息消费者
///
/// 幂等保障: [AllocSyncService#autoSync] 内部基于分账单状态前置校验 + Redis 分布式锁保证同一分账单只同步一次。
/// 异常处理: 消费失败只记 error 日志, 不向上抛出, 避免触发 JMS 默认重试风暴; 漏单由定时同步任务兜底。
@Slf4j
@Component
@RequiredArgsConstructor
public class AllocSyncConsumer {

    private final AllocSyncService allocSyncService;

    @JmsListener(destination = PayArtemisConstants.ALLOC_SYNC_QUEUE)
    public void onMessage(String json) {
        AllocSyncMessage message;
        try {
            // 统一 Text 传输, 消费端手动反序列化为目标类型
            message = JacksonUtil.toBean(json, AllocSyncMessage.class);
        } catch (Exception e) {
            // 消息体解析失败属于毒消息, 直接丢弃避免无限重试
            log.warn("分账同步消息解析失败, 丢弃: json={}, error={}", json, e.getMessage());
            return;
        }
        try {
            allocSyncService.autoSync(message.getAllocNo());
        } catch (Exception e) {
            // 不向上抛出, 避免 JMS 重试风暴; 漏单由定时任务兜底
            log.error("分账同步处理失败, allocNo={}", message.getAllocNo(), e);
        }
    }
}
