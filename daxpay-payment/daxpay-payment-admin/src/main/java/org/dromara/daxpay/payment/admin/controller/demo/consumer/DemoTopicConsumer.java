package org.dromara.daxpay.payment.admin.controller.demo.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.payment.admin.controller.demo.constant.DemoArtemisConstants;
import org.dromara.daxpay.payment.admin.controller.demo.message.DemoArtemisMessage;
import org.dromara.daxpay.payment.admin.controller.demo.result.DemoMessageResult;
import org.dromara.daxpay.payment.admin.controller.demo.store.DemoMessageStore;
import org.dromara.daxpay.platform.common.json.util.JacksonUtil;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/// # 发布订阅消费者（演示）
///
/// 监听 `demo.topic`，演示 JMS pub-sub 广播场景。
/// 同一节点注册两个订阅者，证明发布订阅模式下每个订阅者都能收到完整消息。
///
/// 注意：必须显式指定 `containerFactory = "topicListenerFactory"`，
/// 否则走默认 Queue 工厂，Topic 消息将无法被消费。
@Slf4j
@Component
@RequiredArgsConstructor
public class DemoTopicConsumer {

    private final DemoMessageStore store;

    /// 订阅者 A
    @JmsListener(destination = DemoArtemisConstants.TOPIC, containerFactory = "topicListenerFactory")
    public void onMessageA(String json) {
        handle(json, "demo-topic-consumer-A");
    }

    /// 订阅者 B（同节点模拟多订阅者）
    @JmsListener(destination = DemoArtemisConstants.TOPIC, containerFactory = "topicListenerFactory")
    public void onMessageB(String json) {
        handle(json, "demo-topic-consumer-B");
    }

    private void handle(String json, String consumer) {
        DemoArtemisMessage message;
        try {
            // 统一 Text 传输，消费端手动反序列化为目标类型
            message = JacksonUtil.toBean(json, DemoArtemisMessage.class);
        } catch (Exception e) {
            log.warn("Topic 消息解析失败，忽略: json={}, error={}", json, e.getMessage());
            return;
        }
        DemoMessageResult result = DemoMessageResult.from(message, consumer);
        store.add(result);
        log.info("Topic 消费成功 [{}]: id={}, content={}", consumer, message.getId(), message.getContent());
    }
}
