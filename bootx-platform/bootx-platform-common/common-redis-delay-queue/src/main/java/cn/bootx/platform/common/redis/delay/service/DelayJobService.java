package cn.bootx.platform.common.redis.delay.service;

import cn.bootx.platform.common.redis.delay.bean.DelayJob;
import cn.bootx.platform.common.redis.delay.bean.Job;
import cn.bootx.platform.common.redis.delay.constants.JobStatus;
import cn.bootx.platform.common.redis.delay.container.DelayBucket;
import cn.bootx.platform.common.redis.delay.container.DelayJobPool;
import cn.bootx.platform.common.redis.delay.container.DelayQueue;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 任务服务类
 * @author daify
 * @date 2019-07-28
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DelayJobService {

    private final DelayBucket delayBucket;

    private final DelayQueue delayQueue;

    private final DelayJobPool delayJobPool;

    /**
     * 添加任务, 返回延时任务对象
     */
    public DelayJob addJob(Job<?> job) {
        job.setStatus(JobStatus.DELAY);
        delayJobPool.addOrUpdateJob(job);
        DelayJob delayJob = new DelayJob(job);
        delayBucket.addDelayJob(delayJob);
        return delayJob;
    }

    /**
     * 获取任务
     */
    public Job<?> getProcessJob(String topic) {
        // 从就绪队列拿到任务
        DelayJob delayJob = delayQueue.popJob(topic);
        if (delayJob == null || StrUtil.isBlank(delayJob.getJodId())) {
            return null;
        }
        Job<?> job = delayJobPool.getJob(delayJob.getJodId());
        // 元数据已经删除，则取下一个
        if (job == null) {
            job = getProcessJob(topic);
        } else {
            job.setStatus(JobStatus.RESERVED);
            // 设置再次投递时间, 如果消费失败将会再次投递, 消费成功会删掉任务
            delayJob.setDelayDate(System.currentTimeMillis() + job.getTtrTime());
            delayJobPool.addOrUpdateJob(job);
            delayBucket.addDelayJob(delayJob);
        }
        return job;
    }

    /**
     * 完成一个执行的任务
     */
    public void finishJob(Job<?> job) {
        delayJobPool.removeJob(job.getId());
    }

    /**
     * 删除一个执行的任务
     */
    public void deleteJob(String jobId) {
        delayJobPool.removeJob(jobId);
    }

}
