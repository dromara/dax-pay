package org.dromara.daxpay.payment.admin.controller.demo.message;

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
/// @see org.dromara.daxpay.platform.common.artemis.message.ArtemisMessageConverter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class DemoArtemisMessage {

    /// 业务消息 ID（UUID 去横线）
    private String id;

    /// 消息内容
    private String content;

    /// 消息场景（QUEUE / TOPIC / DELAY / TAG）
    private String scene;

    /// 消息标签（演示 Tag 过滤时使用，其它场景可为空）
    private String tag;

    /// 发送时间（UTC）
    private OffsetDateTime sendTime;
}
