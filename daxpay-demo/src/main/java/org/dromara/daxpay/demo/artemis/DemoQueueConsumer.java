package org.dromara.daxpay.demo.artemis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.platform.common.json.util.JacksonUtil;
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
        DemoMessageResult result = DemoMessageResult.from(message, "demo-queue-consumer");
        store.add(result);
        log.info("Queue 消费成功: id={}, content={}", message.getId(), message.getContent());
    }
}
