package org.dromara.daxpay.payment.admin.controller.demo.config;

import jakarta.jms.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MessageConverter;

/// # Artemis 演示 JMS 监听容器配置
///
/// 为演示 Topic 消费者提供独立的 `JmsListenerContainerFactory`，唯一职责是开启 Topic 广播语义。
///
/// 说明：
/// - `@EnableJms` 已由缓存模块 `CacheJmsListenerConfig` 全局开启，此处无需重复声明
/// - Queue 消费者使用默认 `jmsListenerContainerFactory`（pub-sub-domain=false）
/// - Topic 消费者必须显式指定本工厂
@Configuration
public class DemoJmsListenerConfig {

    /// 演示 Topic 监听容器工厂（pub-sub 模式）
    @Bean(name = "demoTopicListenerFactory")
    public DefaultJmsListenerContainerFactory demoTopicListenerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter) {

        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        // 复用 Artemis 统一的 JSON 转换器
        factory.setMessageConverter(messageConverter);
        // Topic 模式（pub-sub），对应 broker 端 multicast 路由类型
        factory.setPubSubDomain(true);
        factory.setAutoStartup(true);
        return factory;
    }
}
