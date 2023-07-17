package cn.bootx.platform.daxpay.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 项目
 * @author xxm
 * @since 2023/7/17
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "bootx.daxpay")
public class DaxPayProperties {

    /**
     * 消息队列类型
     */
    private MqType mqType = MqType.SPRING;

    /**
     * 消息队列类型
     * @author xxm
     * @since 2023/7/17
     */
    public enum MqType{
        /** Spring 消息 */
        SPRING,
        /** ActiveMQ */
        ACTIVE,
        /** RabbitMQ */
        RABBIT,
        /** RocketMQ */
        ROCKET;
    }
}
