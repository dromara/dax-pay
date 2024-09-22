package cn.bootx.platform.starter.redis.delay.container;

import cn.bootx.platform.starter.redis.delay.bean.DelayJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


/**
 * 任务池, 包括正常任务和死信任务
 * @author daify
 * @date 2019-07-26 14:38
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class DelayJobPool {

    private final RedisTemplate<String, DelayJob<?>> redisTemplate;

    /**
     * 获取正常任务池
     */
    public BoundHashOperations<String,String, DelayJob<?>> getPool () {
        String name = "delay:queue:jobs";
        return redisTemplate.boundHashOps(name);
    }

    /**
     * 添加或更新正常任务
     */
    public void addOrUpdateJob(DelayJob<?> delayJob) {
        this.getPool().put(delayJob.getId(), delayJob);
    }

    /**
     * 获得正常任务
     */
    public DelayJob<?> getJob(String jobId) {
        return getPool().get(jobId);
    }

    /**
     * 移除正常任务
     */
    public void removeJob (String jobId) {
        getPool().delete(jobId);
    }


    /**
     * 获取死信任务池
     */
    public BoundHashOperations<String,String, DelayJob<?>> getDeadPool () {
        String name = "delay:queue:dead:jobs";
        return redisTemplate.boundHashOps(name);
    }

    /**
     * 添加或更新死信任务
     */
    public void addOrUpdateDeadJob(DelayJob<?> delayJob) {
        this.getDeadPool().put(delayJob.getId(), delayJob);
    }

    /**
     * 获得死信任务
     */
    public DelayJob<?> getDeadJob(String jobId) {
        return getDeadPool().get(jobId);
    }

    /**
     * 移除死信任务
     */
    public void removeDelayJob (String jobId) {
        getDeadPool().delete(jobId);
    }
}
