package cn.daxpay.open.demo.artemis.consumer;
import cn.daxpay.open.demo.artemis.service.DemoMessageStore;
import cn.daxpay.open.demo.artemis.result.DemoMessageResult;
import cn.daxpay.open.demo.artemis.model.DemoArtemisMessage;
import cn.daxpay.open.demo.artemis.constants.DemoArtemisConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import cn.daxpay.open.platform.core.code.CommonCode;
import org.slf4j.MDC;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/// # 点对点队列消费者（演示）
///
/// 监听 `demo.queue`，演示 JMS 最经典的点对点场景。
/// 使用默认 `jmsListenerContainerFactory`（pub-sub-domain=false）。
@Slf4j
@Component
@RequiredArgsConstructor
public class DemoQueueConsumer {

    private final DemoMessageStore store;

    /// 接收消息（统一 Text 传输，消费端手动反序列化为目标类型）
    @JmsListener(destination = DemoArtemisConstants.QUEUE)
    public void onMessage(String json) {
        DemoArtemisMessage message;
        try {
            message = JacksonUtil.toBean(json, DemoArtemisMessage.class);
        } catch (Exception e) {
            log.warn("Queue 消息解析失败，忽略: json={}, error={}", json, e.getMessage());
            return;
        }
        // OTel JMS observation 若生效, MDC 已被注入与 producer 相同的 traceId
        String consumerTraceId = MDC.get(CommonCode.TRACE_ID);
        DemoMessageResult result = DemoMessageResult.from(message, "demo-queue-consumer", consumerTraceId);
        store.add(result);
        log.info("Queue 消费成功: id={}, content={}, producerTraceId={}, consumerTraceId={}",
                message.getId(), message.getContent(),
                message.getProducerTraceId(), consumerTraceId);
    }
}
