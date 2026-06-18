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

/// # Tag 过滤消费者（演示）
///
/// 监听 `demo.tag`，演示通过 JMS selector 按消息属性过滤消费。
/// 两个监听方法订阅同一地址，但分别只消费 `tag = 'important'` 和 `tag = 'normal'` 的消息。
///
/// 工作原理：
/// - 生产者发送时在消息属性中写入 `tag`（由 `ArtemisTemplateService.HEADER_TAG = "tag"` 设置）
/// - selector 表达式基于消息属性匹配，`tag` 为字符串需用单引号包裹
@Slf4j
@Component
@RequiredArgsConstructor
public class DemoTagConsumer {

    private final DemoMessageStore store;

    /// 只消费 important 标签的消息
    @JmsListener(destination = DemoArtemisConstants.TAG_QUEUE, selector = "tag = '" + DemoArtemisConstants.TAG_IMPORTANT + "'")
    public void onImportant(String json) {
        handle(json, "demo-tag-consumer-important");
    }

    /// 只消费 normal 标签的消息
    @JmsListener(destination = DemoArtemisConstants.TAG_QUEUE, selector = "tag = '" + DemoArtemisConstants.TAG_NORMAL + "'")
    public void onNormal(String json) {
        handle(json, "demo-tag-consumer-normal");
    }

    private void handle(String json, String consumer) {
        DemoArtemisMessage message;
        try {
            // 统一 Text 传输，消费端手动反序列化为目标类型
            message = JacksonUtil.toBean(json, DemoArtemisMessage.class);
        } catch (Exception e) {
            log.warn("Tag 消息解析失败，忽略: json={}, error={}", json, e.getMessage());
            return;
        }
        DemoMessageResult result = DemoMessageResult.from(message, consumer);
        store.add(result);
        log.info("Tag 消费成功 [{}]: id={}, tag={}, content={}",
                consumer, message.getId(), message.getTag(), message.getContent());
    }
}
