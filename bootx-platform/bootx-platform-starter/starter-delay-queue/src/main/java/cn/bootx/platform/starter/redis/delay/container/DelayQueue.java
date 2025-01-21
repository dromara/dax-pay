package cn.bootx.platform.starter.redis.delay.container;

import cn.bootx.platform.starter.redis.delay.bean.QueueJob;
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

    private final RedisTemplate<String, QueueJob> redisTemplate;

    /**
     * 获取队列key
     */
    public String getKey(String topic) {
        return "delay:queue:ready:topic" + topic;
    }

    /**
     * 获得队列
     */
    public BoundListOperations<String, QueueJob> getQueue (String topic) {
        return redisTemplate.boundListOps(getKey(topic));
    }

    /**
     * 设置任务
     */
    public void  pushJob(QueueJob queueJob) {
        var listOperations = getQueue(queueJob.getTopic());
        listOperations.leftPush(queueJob);
    }

    /**
     * 移除并获得任务
     */
    public QueueJob popJob(String topic) {
        var listOperations = this.getQueue(topic);
        return listOperations.leftPop();
    }

    /**
     * 获取死信队列key
     */
    public String getDeadKey(String topic) {
        return "delay:queue:dead:"+topic;
    }

    /**
     * 获得死信队列
     */
    public BoundListOperations<String, QueueJob> getDeadQueue(String topic) {
        return redisTemplate.boundListOps(getDeadKey(topic));
    }

    /**
     * 设置死信任务
     */
    public void pushDeadJob(QueueJob queueJob) {
        var listOperations = getDeadQueue(queueJob.getTopic());
        listOperations.leftPush(queueJob);
    }

    /**
     * 移除并获得死信任务
     */
    public QueueJob popDeadJob(String topic) {
        var listOperations = this.getDeadQueue(topic);
        return listOperations.leftPop();
    }

}
