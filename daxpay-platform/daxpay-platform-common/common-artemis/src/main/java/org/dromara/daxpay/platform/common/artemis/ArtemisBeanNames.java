package org.dromara.daxpay.platform.common.artemis;

/// # Artemis Bean 名常量
///
/// 集中维护 common-artemis 模块引用到的所有 JMS Bean 名，
/// 供 `@Bean` / `@ConditionalOnMissingBean` / `@Qualifier` /
/// `@JmsListener(containerFactory=...)` 引用，避免散落的字符串字面量。
public interface ArtemisBeanNames {

    /// Queue 发送模板 Bean 名（pubSubDomain=false）
    ///
    /// Spring Boot `JmsAutoConfiguration` 默认装配，非本项目自定义
    String QUEUE_JMS_TEMPLATE = "jmsTemplate";

    /// Topic 发送模板 Bean 名（pubSubDomain=true，本项目自定义）
    String TOPIC_JMS_TEMPLATE = "topicJmsTemplate";

    /// Topic 监听容器工厂 Bean 名（pubSubDomain=true，本项目自定义），
    /// 所有订阅 Topic 的 `@JmsListener` 通过 `containerFactory` 引用
    String TOPIC_LISTENER_FACTORY = "topicListenerFactory";
}
