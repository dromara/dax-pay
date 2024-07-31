package cn.bootx.platform.common.redis.delay.container;

import cn.bootx.platform.common.redis.delay.bean.Job;
import cn.bootx.platform.core.util.JsonUtil;
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

    private final RedisTemplate redisTemplate;

    private String NAME = "job.pool";

    private BoundHashOperations getPool () {
        BoundHashOperations ops = redisTemplate.boundHashOps(NAME);
        return ops;
    }

    /**
     * 添加任务
     * @param job
     */
    public void addJob (Job job) {
        log.info("任务池添加任务：{}", JsonUtil.toJsonStr(job));
        getPool().put(job.getId(),job);
        return ;
    }

    /**
     * 获得任务
     * @param jobId
     */
    public Job getJob(String jobId) {
        Object o = getPool().get(jobId);
        if (o instanceof Job) {
            return (Job) o;
        }
        return null;
    }

    /**
     * 移除任务
     * @param jobId
     */
    public void removeDelayJob (Long jobId) {
        log.info("任务池移除任务：{}",jobId);
        // 移除任务
        getPool().delete(jobId);
    }
}
