package org.dromara.daxpay.platform.capability.cache.configuration;

import org.dromara.daxpay.platform.common.artemis.message.ArtemisMessageConverter;
import jakarta.jms.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MessageConverter;

/// # 缓存失效 JMS 监听容器配置
///
/// 为缓存失效消费者提供独立的 `JmsListenerContainerFactory`，唯一职责是开启 Topic 广播语义。
///
/// 为什么需要独立工厂：
/// - Spring Boot 默认的 `jmsListenerContainerFactory` 是 Queue 模式（pub-sub-domain=false）
/// - `pubSubDomain` 是工厂级配置，无法在 `@JmsListener` 注解上覆盖
/// - 缓存失效必须是 Topic 广播，否则消息只被一个节点消费，导致其他节点 L1 缓存不一致
///
/// 设计取舍：
/// - 不用 durable subscription：缓存失效消息可丢，节点重启后 L1 会重建，有 TTL 兜底无需补收
/// - 不设 clientId：non-durable 订阅不强制 clientId 唯一，省去 hostname+PID 拼接的出错面
@Configuration
@EnableJms
public class CacheJmsListenerConfig {

    /// 缓存失效 Topic 监听容器工厂
    @Bean(name = "cacheTopicListenerFactory")
    public DefaultJmsListenerContainerFactory cacheTopicListenerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter) {

        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        // 使用 Artemis 统一的 JSON 转换器（@ConditionalOnMissingBean 保证此处注入的是 ArtemisMessageConverter）
        factory.setMessageConverter(messageConverter);
        // Topic 模式（pub-sub），对应 broker 端 multicast 路由类型 —— 本工厂存在的唯一理由
        factory.setPubSubDomain(true);
        factory.setAutoStartup(true);
        return factory;
    }
}
