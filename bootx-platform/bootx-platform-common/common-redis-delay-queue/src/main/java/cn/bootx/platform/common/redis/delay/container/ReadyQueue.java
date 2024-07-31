package cn.bootx.platform.common.redis.delay.container;

import cn.bootx.platform.common.redis.delay.bean.DelayJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 就绪队列
 * @author daify
 * @date 2019-08-08 14:07
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class ReadyQueue {

    private final RedisTemplate<String,DelayJob> redisTemplate;

    private String NAME = "process:queue:";

    private String getKey(String topic) {
        return NAME + topic;
    }

    /**
     * 获得队列
     */
    private BoundListOperations<String, DelayJob> getQueue (String topic) {
        return redisTemplate.boundListOps(getKey(topic));
    }

    /**
     * 设置任务
     */
    public void  pushJob(DelayJob delayJob) {
        var listOperations = getQueue(delayJob.getTopic());
        listOperations.leftPush(delayJob);
    }

    /**
     * 移除并获得任务
     */
    public DelayJob popJob(String topic) {
        var listOperations = this.getQueue(topic);
        var job = listOperations.leftPop();
        return job;
    }

}
