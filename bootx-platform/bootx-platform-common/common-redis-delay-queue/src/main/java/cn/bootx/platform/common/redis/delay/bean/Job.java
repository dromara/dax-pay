package cn.bootx.platform.common.redis.delay.bean;

import cn.bootx.platform.common.redis.delay.constants.JobStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 任务
 * @author daify
 * @date 2019-07-25 15:19
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Job<T> implements Serializable {

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
     * 任务的执行超时时间
     */
    private long ttrTime;

    /**
     * 任务具体的消息内容，用于处理具体业务逻辑用
     */
    private T message;

    /**
     * 重试次数
     */
    private int retryCount;
    /**
     * 任务状态
     */
    private JobStatus status;
}
