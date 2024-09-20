package cn.bootx.platform.starter.redis.delay.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 延迟任务
 * @author daify
 * @date 2019-07-25 15:24
 **/
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class QueueJob implements Serializable {

    /**
     * 延迟任务的唯一标识
     */
    private String jodId;

    /**
     * 任务的执行时间
     */
    private long delayDate;

    /**
     * 任务类型（具体业务类型）
     */
    private String topic;

    public QueueJob(DelayJob<?> delayJob) {
        this.jodId = delayJob.getId();
        this.delayDate = System.currentTimeMillis() + delayJob.getDelayTime();
        this.topic = delayJob.getTopic();
    }
}
