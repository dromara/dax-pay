package cn.bootx.platform.starter.redis.delay.annotation;

import cn.bootx.platform.starter.redis.delay.constants.JobStatus;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 延时任务数据类
 * @author xxm
 * @since 2024/7/31
 */
@Data
@Accessors(chain = true)
public class DelayJobEvent<T>{

    /**
     * 延迟任务的唯一标识，用于检索任务
     */
    private String id;

    /**
     * 任务类型（具体业务类型）
     */
    private String topic;

    /**
     * 任务的延迟时间
     */
    private long delayTime;

    /**
     * 再次投递时间
     */
    private long ttrTime;

    /**
     * 任务具体的消息内容，用于处理具体业务逻辑用
     */
    private T message;

    /**
     * 任务状态
     */
    private JobStatus status;
}
