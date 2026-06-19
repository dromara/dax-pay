package org.dromara.daxpay.demo.artemis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # Artemis 演示消息体
///
/// 通过 JMS 在生产者与消费者之间传递的消息载体。
/// 必须保留无参构造，否则 Jackson 反序列化会失败。
///
/// 传输时由生产端用 `JacksonUtil.toJson` 序列化为 JSON 字符串，
/// 消费端 `onMessage(String json)` 拿到文本后自行 `JacksonUtil.toBean` 反序列化。
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class DemoArtemisMessage {

    /// 业务消息 ID（UUID 去横线）
    private String id;

    /// 消息内容
    private String content;

    /// 消息场景（QUEUE / TOPIC / DELAY）
    private String scene;

    /// 发送时间（UTC）
    private OffsetDateTime sendTime;
}
