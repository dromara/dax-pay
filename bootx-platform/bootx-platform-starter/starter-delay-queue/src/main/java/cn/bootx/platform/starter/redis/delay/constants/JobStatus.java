package cn.bootx.platform.starter.redis.delay.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务状态
 * @author daify
 * @date 2019-07-26 16:03
 **/
@Getter
@AllArgsConstructor
public enum JobStatus {
    /**
     * 不可执行状态，等待时钟周期
     */
    DELAY("delay"),
    /**
     * 可执行状态，等待消费
     */
    READY("ready"),
    /**
     * 已被消费者读取，但还未得到消费者的响应
     */
    RESERVED("reserved"),
    /**
     * 死信
     */
    DEAD("dead");

    private final String code;

}
