package org.dromara.daxpay.platform.common.artemis;

import jakarta.jms.ConnectionFactory;
import org.dromara.daxpay.platform.common.artemis.message.ArtemisMessageConverter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.jms.autoconfigure.JmsAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;

/// # Artemis 通用模块自动配置入口
///
/// 通过 SPI（`META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`）加载。
///
/// 注册内容：
/// - 组件扫描加载 {@link org.dromara.daxpay.platform.common.artemis.service.ArtemisTemplateService}
/// - 注册 {@link ArtemisMessageConverter} 为默认 {@link MessageConverter}，
///   使 JmsTemplate / JmsClient 的 `send(Object payload)` 自动序列化为 JSON TextMessage
/// - 注册独立的 `topicJmsTemplate`（pubSubDomain=true），供 Topic 广播场景使用
///
/// 约束：
/// - 必须 `after = JmsAutoConfiguration.class`，否则我们的 `topicJmsTemplate`（也是 JmsTemplate 类型）
///   会让 Spring Boot 内部的 `@ConditionalOnMissingBean(JmsTemplate.class)` 不通过，
///   导致默认 `jmsTemplate` Bean 不被创建，业务方注入时报 `No qualifying bean ... @Qualifier("jmsTemplate")`
/// - broker 端需保证相关 address 存在；若开启 `auto-create-jms-queues/addresses`，则发送时自动创建
/// - 广播场景（对应 RocketMQ BROADCASTING）要求 broker 端把 address 配为 multicast 路由类型，
///   并在客户端用独立 durable subscription 名
@AutoConfiguration(after = JmsAutoConfiguration.class)
@ComponentScan
public class ArtemisCommonAutoConfiguration {

    /// Topic 模板的 Bean 名，避免与 Spring Boot 默认的 `jmsTemplate` 冲突
    public static final String TOPIC_JMS_TEMPLATE = "topicJmsTemplate";

    /// 默认消息转换器：统一使用 JSON + TextMessage
    ///
    /// 仅当容器中不存在其他 MessageConverter 时生效，避免覆盖业务自定义实现
    @Bean
    @ConditionalOnMissingBean(MessageConverter.class)
    public ArtemisMessageConverter artemisMessageConverter() {
        return new ArtemisMessageConverter();
    }

    /// Topic 专用 JmsTemplate（pubSubDomain=true）
    ///
    /// 为什么需要独立 Bean：
    /// - Spring Boot 自动装配的 `jmsTemplate` 由 `spring.jms.pub-sub-domain` 决定 destination 类型
    /// - 项目默认 `pub-sub-domain=false`（点对点 Queue），发到 Topic 地址会触发 Artemis broker
    ///   `Destination ... does not support ANYCAST routing` 异常
    /// - `pubSubDomain` 是 JmsTemplate 实例级配置，无法在调用时切换，必须独立 Bean
    ///
    /// `@ConditionalOnMissingBean(name = TOPIC_JMS_TEMPLATE)`：仅当业务方未自定义同名 Bean 时生效。
    /// 复用统一的 {@link MessageConverter}，序列化行为与默认模板保持一致。
    @Bean(TOPIC_JMS_TEMPLATE)
    @ConditionalOnMissingBean(name = TOPIC_JMS_TEMPLATE)
    public JmsTemplate topicJmsTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        JmsTemplate template = new JmsTemplate(connectionFactory);
        // 关键：开启 pub-sub 模式，destination 解析为 Topic（multicast）
        template.setPubSubDomain(true);
        template.setMessageConverter(messageConverter);
        return template;
    }
}
