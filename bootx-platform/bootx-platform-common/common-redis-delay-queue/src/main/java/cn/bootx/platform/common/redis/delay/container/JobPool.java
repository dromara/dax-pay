package cn.bootx.platform.common.redis.delay.container;

import cn.bootx.platform.common.redis.delay.bean.Job;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


/**
 * 任务池
 * @author daify
 * @date 2019-07-26 14:38
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class JobPool {

    private final RedisTemplate<String,Job<?>> redisTemplate;

    private String NAME = "job:pool:";

    private BoundHashOperations<String,String,Job<?>> getPool () {
        return redisTemplate.boundHashOps(NAME);
    }

    /**
     * 添加任务
     */
    public void addJob (Job<?> job) {
        this.getPool().put(job.getId(),job);
    }

    /**
     * 获得任务
     */
    public Job<?> getJob(String jobId) {
        return getPool().get(jobId);
    }

    /**
     * 移除任务
     */
    public void removeDelayJob (String jobId) {
        log.info("任务池移除任务：{}",jobId);
        getPool().delete(jobId);
    }
}
