package cn.bootx.platform.common.redis.delay.container;

import cn.bootx.platform.common.redis.delay.bean.Job;
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

    private final RedisTemplate<String,Job<?>> redisTemplate;

    /**
     * 获取正常任务池
     */
    private BoundHashOperations<String,String,Job<?>> getPool () {
        String name = "delay:queue:jobs";
        return redisTemplate.boundHashOps(name);
    }

    /**
     * 添加或更新正常任务
     */
    public void addOrUpdateJob(Job<?> job) {
        this.getPool().put(job.getId(),job);
    }

    /**
     * 获得正常任务
     */
    public Job<?> getJob(String jobId) {
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
    private BoundHashOperations<String,String,Job<?>> getDeadPool () {
        String name = "delay:queue:dead:jobs";
        return redisTemplate.boundHashOps(name);
    }

    /**
     * 添加或更新死信任务
     */
    public void addOrUpdateDeadJob(Job<?> job) {
        this.getDeadPool().put(job.getId(),job);
    }

    /**
     * 获得死信任务
     */
    public Job<?> getDeadJob(String jobId) {
        return getDeadPool().get(jobId);
    }

    /**
     * 移除死信任务
     */
    public void removeDelayJob (String jobId) {
        getDeadPool().delete(jobId);
    }
}
