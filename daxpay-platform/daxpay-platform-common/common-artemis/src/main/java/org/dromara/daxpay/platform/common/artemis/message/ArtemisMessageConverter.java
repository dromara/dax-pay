package org.dromara.daxpay.platform.common.artemis.message;

import org.dromara.daxpay.platform.common.artemis.exception.ArtemisException;
import org.dromara.daxpay.platform.common.json.util.JacksonUtil;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

/// # Artemis 消息转换器
///
/// 统一使用 TextMessage + JSON 文本承载消息，实现「纯 Text 传输」。
///
/// 设计要点：
/// - 发送端任意对象统一由 Jackson 序列化为 JSON 字符串，写入 TextMessage
/// - 不再向消息属性写入 Java 类型信息（如 `_type`），避免生产/消费两端类型绑定
/// - 消费端 `@JmsListener` 方法签名统一为 `onMessage(String json)`，自行调用 `JacksonUtil.toBean` 反序列化
/// - 这样消息保持自描述的 JSON 契约，类改名、跨服务消费、多语言接入都安全
///
/// @see MessageConverter Spring JMS 标准消息转换接口
@Slf4j
public class ArtemisMessageConverter implements MessageConverter {

    /// 序列化目标：使用 TextMessage 承载 JSON
    @Override
    public Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {
        String json;
        try {
            // 非格式化 JSON，节省传输体积
            json = JacksonUtil.toJson(object, false);
        } catch (Exception e) {
            log.error("Artemis 消息序列化失败: {}", e.getMessage(), e);
            throw new ArtemisException("error.artemis.serializeFailed", e.getMessage());
        }

        // 仅写入 JSON 文本，不携带任何类型属性，保持消息与 Java 类型解耦
        return session.createTextMessage(json);
    }

    /// 反序列化：直接返回 TextMessage 的文本内容
    ///
    /// 永远返回 String（JSON 文本），由消费端按需手动反序列化为目标类型。
    /// 因此 `@JmsListener` 方法签名统一使用 `onMessage(String json)`。
    @Override
    public Object fromMessage(Message message) throws JMSException, MessageConversionException {
        if (!(message instanceof TextMessage textMessage)) {
            log.error("Artemis 消息体类型不支持，期望 TextMessage，实际: {}", message.getClass().getName());
            throw new ArtemisException("error.artemis.parseBodyFailed",
                    "expected TextMessage but got " + message.getClass().getName());
        }
        return textMessage.getText();
    }
}
