package org.dromara.daxpay.demo.artemis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.platform.common.json.util.JacksonUtil;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/// # 延时消息消费者（演示）
///
/// 监听 `demo.delay`，演示 Artemis 延时投递能力（订单超时关单等场景）。
/// 消息由生产者通过 `ArtemisTemplateService.sendDelay(...)` 延时投递。
///
/// 注意：演示记录中的 `costMillis` 可显著大于普通消息，正是延时效果的体现。
@Slf4j
@Component
@RequiredArgsConstructor
public class DemoDelayConsumer {

    private final DemoMessageStore store;

    @JmsListener(destination = DemoArtemisConstants.DELAY_QUEUE)
    public void onMessage(String json) {
        DemoArtemisMessage message;
        try {
            // 统一 Text 传输，消费端手动反序列化为目标类型
            message = JacksonUtil.toBean(json, DemoArtemisMessage.class);
        } catch (Exception e) {
            log.warn("Delay 消息解析失败，忽略: json={}, error={}", json, e.getMessage());
            return;
        }
        DemoMessageResult result = DemoMessageResult.from(message, "demo-delay-consumer");
        store.add(result);
        log.info("Delay 消费成功: id={}, content={}, costMillis={}",
                message.getId(), message.getContent(), result.getCostMillis());
    }
}
