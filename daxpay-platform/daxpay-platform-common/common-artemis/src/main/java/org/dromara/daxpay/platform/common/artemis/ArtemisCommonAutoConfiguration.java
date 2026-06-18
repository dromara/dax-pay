package org.dromara.daxpay.platform.common.artemis;

import org.dromara.daxpay.platform.common.artemis.message.ArtemisMessageConverter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.support.converter.MessageConverter;

/// # Artemis 通用模块自动配置入口
///
/// 通过 SPI（`META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`）加载。
///
/// 注册内容：
/// - 组件扫描加载 {@link org.dromara.daxpay.platform.common.artemis.service.ArtemisTemplateService}
/// - 注册 {@link ArtemisMessageConverter} 为默认 {@link MessageConverter}，
///   使 JmsTemplate / JmsClient 的 `send(Object payload)` 自动序列化为 JSON TextMessage
///
/// 约束：
/// - broker 端需保证相关 address 存在；若开启 `auto-create-jms-queues/addresses`，则发送时自动创建
/// - 广播场景（对应 RocketMQ BROADCASTING）要求 broker 端把 address 配为 multicast 路由类型，
///   并在客户端用独立 durable subscription 名
@AutoConfiguration
@ComponentScan
public class ArtemisCommonAutoConfiguration {

    /// 默认消息转换器：统一使用 JSON + TextMessage
    ///
    /// 仅当容器中不存在其他 MessageConverter 时生效，避免覆盖业务自定义实现
    @Bean
    @ConditionalOnMissingBean(MessageConverter.class)
    public ArtemisMessageConverter artemisMessageConverter() {
        return new ArtemisMessageConverter();
    }
}
