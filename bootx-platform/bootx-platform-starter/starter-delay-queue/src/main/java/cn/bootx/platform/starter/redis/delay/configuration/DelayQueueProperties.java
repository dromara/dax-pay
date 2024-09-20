package cn.bootx.platform.starter.redis.delay.configuration;

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
    private Integer bucketCount = 3;

    /**
     * 轮训睡眠时间 默认一秒
     */
    private Long sleepTime = 1000L;

    /**
     * 重试次数 默认五次
     */
    private Integer retryCount = 5;

    /**
     * 默认超时时间 默认五秒
     */
    private Long processTime = 5000L;

    /**
     * 失败或超时后多久重新投递 默认五秒
     */
    private Long retryTime = 5000L;
}
