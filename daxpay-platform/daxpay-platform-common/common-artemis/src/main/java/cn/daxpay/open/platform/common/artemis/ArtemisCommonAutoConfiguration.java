package cn.daxpay.open.platform.common.artemis;

import jakarta.jms.ConnectionFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.jms.autoconfigure.JmsAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

/// # Artemis 通用模块自动配置入口
///
/// 通过 SPI（`META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`）加载。
///
/// 注册内容：
/// - 组件扫描加载 {@link cn.daxpay.open.platform.common.artemis.service.ArtemisTemplateService}
/// - `@EnableJms`：全局开启 `@JmsListener` 注解处理
/// - `topicJmsTemplate`（pubSubDomain=true）：发送端 Topic 能力
/// - `topicListenerFactory`（pubSubDomain=true）：消费端 Topic 能力
///
/// 为什么 Queue / Topic 必须分两套 Bean：
/// - `pubSubDomain` 是 JmsTemplate / JmsListenerContainerFactory 的实例级配置，
///   决定 destination 解析为 Queue（anycast）还是 Topic（multicast）
/// - Spring Boot 默认装配的 `jmsTemplate` / `jmsListenerContainerFactory` 是 Queue 模式
///   （由 `spring.jms.pub-sub-domain=false` 决定）
/// - Topic 场景必须用本类提供的 topic 版本，否则 broker 端路由类型不匹配
///
/// 消息传输约定：
/// - 消息体统一为 JSON 字符串，由调用方自行序列化/反序列化
/// - 不注册 MessageConverter，回落到 Spring 默认的 `SimpleMessageConverter`
///   —— 对 String payload 直接 `createTextMessage(string)`，对 `onMessage(String)` 直接返回文本
///
/// 约束：
/// - 必须 `after = JmsAutoConfiguration.class`，否则我们的 `topicJmsTemplate`（也是 JmsTemplate 类型）
///   会让 Spring Boot 内部的 `@ConditionalOnMissingBean(JmsTemplate.class)` 不通过，
///   导致默认 `jmsTemplate` Bean 不被创建，业务方注入时报 `No qualifying bean ... @Qualifier("jmsTemplate")`
/// - broker 端需保证相关 address 存在；若开启 `auto-create-jms-queues/addresses`，则发送时自动创建
/// - 广播场景（对应 RocketMQ BROADCASTING）要求 broker 端把 address 配为 multicast 路由类型
@AutoConfiguration(after = JmsAutoConfiguration.class)
@ComponentScan
@EnableJms
public class ArtemisCommonAutoConfiguration {

    /// Topic 发送专用 JmsTemplate（pubSubDomain=true）
    ///
    /// 与 Spring Boot 默认的 `jmsTemplate`（Queue 模式）共存，
    /// 由 `ArtemisTemplateService` 通过 `@Qualifier` 注入。
    ///
    /// 不设置 MessageConverter，回落到 Spring 默认 `SimpleMessageConverter`（String ↔ TextMessage 透传）。
    @Bean(ArtemisBeanNames.TOPIC_JMS_TEMPLATE)
    @ConditionalOnMissingBean(name = ArtemisBeanNames.TOPIC_JMS_TEMPLATE)
    public JmsTemplate topicJmsTemplate(ConnectionFactory connectionFactory) {
        JmsTemplate template = new JmsTemplate(connectionFactory);
        // 关键：开启 pub-sub 模式，destination 解析为 Topic（multicast）
        template.setPubSubDomain(true);
        return template;
    }

    /// Topic 消费专用监听容器工厂（pubSubDomain=true）
    ///
    /// 所有需要订阅 Topic 的 `@JmsListener` 通过
    /// `containerFactory = "topicListenerFactory"` 引用，
    /// 取代各业务模块自行创建重复的 Topic ListenerFactory。
    @Bean(ArtemisBeanNames.TOPIC_LISTENER_FACTORY)
    @ConditionalOnMissingBean(name = ArtemisBeanNames.TOPIC_LISTENER_FACTORY)
    public JmsListenerContainerFactory<?> topicListenerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        // Topic 模式（pub-sub），对应 broker 端 multicast 路由类型
        factory.setPubSubDomain(true);
        factory.setAutoStartup(true);
        return factory;
    }
}
