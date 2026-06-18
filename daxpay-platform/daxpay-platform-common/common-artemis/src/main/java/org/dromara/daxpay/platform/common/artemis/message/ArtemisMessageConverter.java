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
/// 统一消息对象与 JSON 之间的转换，复用项目现有 Jackson 能力。
///
/// 设计要点：
/// - 消息体统一使用 TextMessage + JSON 字符串，避免 ObjectMessage 的 serialVersionUID 兼容问题
/// - 通过 `_type` 属性记录原始类型，消费端可据此还原为对象
/// - 实现 Spring 的 {@link MessageConverter}，可被 JmsTemplate / JmsClient 自动调用
///
/// @see MessageConverter Spring JMS 标准消息转换接口
@Slf4j
public class ArtemisMessageConverter implements MessageConverter {

    /// 消息体类型属性名，消费端据此反序列化
    public static final String TYPE_PROPERTY = "_type";

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

        TextMessage textMessage = session.createTextMessage(json);
        // 记录原始类型，消费端可据此反序列化
        if (object != null) {
            textMessage.setStringProperty(TYPE_PROPERTY, object.getClass().getName());
        }
        return textMessage;
    }

    /// 反序列化：TextMessage 的 JSON 还原为对象
    ///
    /// 若消费端直接以 String 接收（如 `@JmsListener` 方法签名为 `onMessage(String json)`），
    /// Spring 不会走此方法，直接返回文本内容。
    @Override
    public Object fromMessage(Message message) throws JMSException, MessageConversionException {
        if (!(message instanceof TextMessage textMessage)) {
            log.error("Artemis 消息体类型不支持，期望 TextMessage，实际: {}", message.getClass().getName());
            throw new ArtemisException("error.artemis.parseBodyFailed",
                    "expected TextMessage but got " + message.getClass().getName());
        }

        String json = textMessage.getText();
        String typeName = message.getStringProperty(TYPE_PROPERTY);
        if (typeName == null) {
            // 无类型信息时直接返回 JSON 字符串
            return json;
        }

        try {
            Class<?> bodyClass = Class.forName(typeName);
            return JacksonUtil.toBean(json, bodyClass);
        } catch (ClassNotFoundException e) {
            // 多应用场景下消费端可能没有该类，降级返回 JSON 字符串
            log.warn("Artemis 消息类型 {} 未找到，降级返回 JSON 字符串", typeName);
            return json;
        } catch (Exception e) {
            log.error("Artemis 消息体解析失败: {}", e.getMessage(), e);
            throw new ArtemisException("error.artemis.parseBodyFailed", e.getMessage());
        }
    }
}
