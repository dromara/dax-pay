package cn.bootx.platform.common.redis.delay.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 延时队列配置
 * @author xxm
 * @since 2024/7/31
 */
@ConfigurationProperties(prefix = "bootx-platform.common.delay-queue")
@Getter
@Setter
public class DelayQueueProperties {

    /**
     * 桶数量, 每一个桶会开启一个定时任务
     */
    private Integer bucketCount = 5;

    /**
     * 轮训睡眠时间
     */
    private Long sleepTime = 1000L;

    /**
     * 重试次数
     */
    private Integer retryCount = 5;

    /**
     * 默认超时时间
     */
    public Long processTime = 5000L;
}
