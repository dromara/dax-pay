package org.dromara.daxpay.platform.common.artemis.service;

import org.dromara.daxpay.platform.common.artemis.ArtemisCommonAutoConfiguration;
import org.dromara.daxpay.platform.common.artemis.exception.ArtemisException;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.JmsClient;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

/// # Artemis 统一发送服务
///
/// 基于 Spring Framework 7 的 {@link JmsClient} 封装 JMS 消息发送能力，提供统一发送入口。
///
/// 设计要点：
/// - 底层走 JMS 2.0 标准 API，不绑定 Artemis 私有属性，可移植到任何 JMS 2.0 broker
/// - 区分 Queue / Topic 两类语义：
///   - `send*` 系列方法：点对点 Queue（anycast）
///   - `sendTopic*` 系列方法：发布订阅 Topic（multicast）
/// - 延时消息使用 `JmsClient.withDeliveryDelay()`，对应 JMS 2.0 `MessageProducer.setDeliveryDelay()`
/// - 业务幂等键通过 `KEYS` 属性传递（沿用 RocketMQ 时期的命名约定）
/// - tag 作为消息属性保留，消费端可用 JMS selector `tag IN ('x','y')` 过滤
///
/// 为什么 Queue / Topic 必须分开方法：
/// - `pubSubDomain` 是 JmsTemplate 实例级配置，决定 destination 解析方式
/// - 错配会触发 broker 端 `Destination ... does not support ANYCAST/MULTICAST routing` 异常
/// - 调用方在编码期就明确语义，避免运行期歧义
///
/// @see JmsClient Spring Framework 7 fluent JMS 客户端
@Slf4j
@Service
public class ArtemisTemplateService {

    /// 业务幂等键属性名（沿用原 RocketMQ 命名，便于追踪）
    public static final String HEADER_KEYS = "KEYS";

    /// 消息标签属性名，用于消费端 selector 过滤
    public static final String HEADER_TAG = "tag";

    /// Queue 模板（pubSubDomain=false），由 Spring Boot 自动装配，Bean 名 `jmsTemplate`
    private final JmsTemplate queueJmsTemplate;

    /// Topic 模板（pubSubDomain=true），见 [ArtemisCommonAutoConfiguration#topicJmsTemplate]
    private final JmsTemplate topicJmsTemplate;

    public ArtemisTemplateService(@Qualifier("jmsTemplate") JmsTemplate queueJmsTemplate,
                                  @Qualifier(ArtemisCommonAutoConfiguration.TOPIC_JMS_TEMPLATE) JmsTemplate topicJmsTemplate) {
        this.queueJmsTemplate = queueJmsTemplate;
        this.topicJmsTemplate = topicJmsTemplate;
    }

    // ============================== Queue 点对点 ==============================

    /// 同步发送消息（点对点 Queue）
    ///
    /// @param address 目标地址（对应 Artemis address，kebab-case 命名）
    /// @param tag     消息标签，用于消息过滤，为空时不设置
    /// @param body    消息体，由 MessageConverter 自动序列化为 JSON TextMessage
    public void send(String address, String tag, Object body) {
        Map<String, Object> headers = buildHeaders(tag);
        try {
            jmsClient(queueJmsTemplate).destination(address).send(body, headers);
            log.debug("Queue 消息发送成功: address={}, tag={}, keys={}, body={}",
                    address, tag, headers.get(HEADER_KEYS), body);
        } catch (MessagingException e) {
            log.error("Queue 消息发送异常: address={}, tag={}, error={}", address, tag, e.getMessage(), e);
            throw new ArtemisException("error.artemis.sendFailed", e.getMessage());
        }
    }

    /// 发送延时消息（点对点 Queue，自定义延时时间）
    ///
    /// 基于 JMS 2.0 标准 deliveryDelay，Artemis 原生支持任意毫秒级精度。
    /// 底层等价于 `MessageProducer.setDeliveryDelay(delayMillis)`。
    ///
    /// @param address        目标地址
    /// @param tag            消息标签
    /// @param body           消息体
    /// @param delaySeconds   延时秒数
    public void sendDelay(String address, String tag, Object body, int delaySeconds) {
        Map<String, Object> headers = buildHeaders(tag);
        long delayMillis = delaySeconds * 1000L;
        try {
            jmsClient(queueJmsTemplate).destination(address)
                    .withDeliveryDelay(delayMillis)
                    .send(body, headers);
            log.info("Queue 延时消息发送成功: address={}, tag={}, keys={}, delaySeconds={}, body={}",
                    address, tag, headers.get(HEADER_KEYS), delaySeconds, body);
        } catch (MessagingException e) {
            log.error("Queue 延时消息发送异常: address={}, tag={}, delaySeconds={}, error={}",
                    address, tag, delaySeconds, e.getMessage(), e);
            throw new ArtemisException("error.artemis.delaySendFailed", e.getMessage());
        }
    }

    /// 发送定时投递消息（点对点 Queue，指定投递时刻）
    ///
    /// 为将来恢复延时业务（订单超时关单、回调重试等）预留的便捷方法。
    /// 内部换算为相对当前时间的延时毫秒数。
    ///
    /// @param address       目标地址
    /// @param tag           消息标签
    /// @param body          消息体
    /// @param deliveryTime  投递时刻（统一使用 OffsetDateTime，避免时区问题）
    public void sendDelayAt(String address, String tag, Object body, OffsetDateTime deliveryTime) {
        Map<String, Object> headers = buildHeaders(tag);
        long deliveryTimestamp = deliveryTime.toInstant().toEpochMilli();
        long delayMillis = deliveryTimestamp - System.currentTimeMillis();
        if (delayMillis < 0) {
            // 投递时刻已过，立即发送
            delayMillis = 0;
        }
        try {
            jmsClient(queueJmsTemplate).destination(address)
                    .withDeliveryDelay(delayMillis)
                    .send(body, headers);
            log.info("Queue 定时消息发送成功: address={}, tag={}, keys={}, deliveryTime={}, delayMillis={}",
                    address, tag, headers.get(HEADER_KEYS), deliveryTime, delayMillis);
        } catch (MessagingException e) {
            log.error("Queue 定时消息发送异常: address={}, tag={}, deliveryTime={}, error={}",
                    address, tag, deliveryTime, e.getMessage(), e);
            throw new ArtemisException("error.artemis.delaySendFailed", e.getMessage());
        }
    }

    // ============================== Topic 发布订阅 ==============================

    /// 同步发送广播消息（发布订阅 Topic）
    ///
    /// broker 端对应 address 必须配置为 multicast 路由类型，否则会触发
    /// `Destination ... does not support MULTICAST routing` 异常。
    ///
    /// @param address 目标 Topic 地址
    /// @param tag     消息标签
    /// @param body    消息体
    public void sendTopic(String address, String tag, Object body) {
        Map<String, Object> headers = buildHeaders(tag);
        try {
            jmsClient(topicJmsTemplate).destination(address).send(body, headers);
            log.debug("Topic 消息发送成功: address={}, tag={}, keys={}, body={}",
                    address, tag, headers.get(HEADER_KEYS), body);
        } catch (MessagingException e) {
            log.error("Topic 消息发送异常: address={}, tag={}, error={}", address, tag, e.getMessage(), e);
            throw new ArtemisException("error.artemis.sendFailed", e.getMessage());
        }
    }

    /// 发送 Topic 延时消息
    ///
    /// @param address       目标 Topic 地址
    /// @param tag           消息标签
    /// @param body          消息体
    /// @param delaySeconds  延时秒数
    public void sendTopicDelay(String address, String tag, Object body, int delaySeconds) {
        Map<String, Object> headers = buildHeaders(tag);
        long delayMillis = delaySeconds * 1000L;
        try {
            jmsClient(topicJmsTemplate).destination(address)
                    .withDeliveryDelay(delayMillis)
                    .send(body, headers);
            log.info("Topic 延时消息发送成功: address={}, tag={}, keys={}, delaySeconds={}, body={}",
                    address, tag, headers.get(HEADER_KEYS), delaySeconds, body);
        } catch (MessagingException e) {
            log.error("Topic 延时消息发送异常: address={}, tag={}, delaySeconds={}, error={}",
                    address, tag, delaySeconds, e.getMessage(), e);
            throw new ArtemisException("error.artemis.delaySendFailed", e.getMessage());
        }
    }

    /// 发送 Topic 定时投递消息
    ///
    /// @param address       目标 Topic 地址
    /// @param tag           消息标签
    /// @param body          消息体
    /// @param deliveryTime  投递时刻
    public void sendTopicDelayAt(String address, String tag, Object body, OffsetDateTime deliveryTime) {
        Map<String, Object> headers = buildHeaders(tag);
        long deliveryTimestamp = deliveryTime.toInstant().toEpochMilli();
        long delayMillis = deliveryTimestamp - System.currentTimeMillis();
        if (delayMillis < 0) {
            // 投递时刻已过，立即发送
            delayMillis = 0;
        }
        try {
            jmsClient(topicJmsTemplate).destination(address)
                    .withDeliveryDelay(delayMillis)
                    .send(body, headers);
            log.info("Topic 定时消息发送成功: address={}, tag={}, keys={}, deliveryTime={}, delayMillis={}",
                    address, tag, headers.get(HEADER_KEYS), deliveryTime, delayMillis);
        } catch (MessagingException e) {
            log.error("Topic 定时消息发送异常: address={}, tag={}, deliveryTime={}, error={}",
                    address, tag, deliveryTime, e.getMessage(), e);
            throw new ArtemisException("error.artemis.delaySendFailed", e.getMessage());
        }
    }

    // ============================== 内部工具 ==============================

    /// 构建消息头：业务幂等键 + 标签
    private Map<String, Object> buildHeaders(String tag) {
        Map<String, Object> headers = new HashMap<>(4);
        // 随机 UUID 作为业务幂等键，去横线
        headers.put(HEADER_KEYS, UUID.randomUUID().toString(true));
        if (StrUtil.isNotEmpty(tag)) {
            headers.put(HEADER_TAG, tag);
        }
        return headers;
    }

    /// 构建 JmsClient 实例（每次发送新建，轻量对象）
    private JmsClient jmsClient(JmsTemplate template) {
        return JmsClient.create(template);
    }
}
