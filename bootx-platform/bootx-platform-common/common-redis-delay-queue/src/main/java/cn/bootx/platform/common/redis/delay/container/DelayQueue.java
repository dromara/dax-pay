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
public class DelayQueue {

    private final RedisTemplate<String,DelayJob> redisTemplate;

    /**
     * 获取队列key
     */
    private String getKey(String topic) {
        return "delay:queue:ready:topic" + topic;
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
        return listOperations.leftPop();
    }

    /**
     * 获取死信队列key
     */
    private String getDeadKey(String topic) {
        return "delay:queue:dead:"+topic;
    }

    /**
     * 获得死信队列
     */
    private BoundListOperations<String, DelayJob> getDeadQueue(String topic) {
        return redisTemplate.boundListOps(getDeadKey(topic));
    }

    /**
     * 设置死信任务
     */
    public void  pushDeadJob(DelayJob delayJob) {
        var listOperations = getDeadQueue(delayJob.getTopic());
        listOperations.leftPush(delayJob);
    }

    /**
     * 移除并获得死信任务
     */
    public DelayJob popDeadJob(String topic) {
        var listOperations = this.getDeadQueue(topic);
        return listOperations.leftPop();
    }

}
