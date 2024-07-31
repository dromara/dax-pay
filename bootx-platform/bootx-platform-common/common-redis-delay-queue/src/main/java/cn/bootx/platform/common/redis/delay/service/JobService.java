package cn.bootx.platform.common.redis.delay.service;

import cn.bootx.platform.common.redis.delay.bean.DelayJob;
import cn.bootx.platform.common.redis.delay.bean.Job;
import cn.bootx.platform.common.redis.delay.constants.JobStatus;
import cn.bootx.platform.common.redis.delay.container.DelayBucket;
import cn.bootx.platform.common.redis.delay.container.JobPool;
import cn.bootx.platform.common.redis.delay.container.ReadyQueue;
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
public class JobService {

    private final DelayBucket delayBucket;

    private final ReadyQueue readyQueue;

    private final JobPool jobPool;


    public DelayJob addDefJob(Job<?> job) {
        job.setStatus(JobStatus.DELAY);
        jobPool.addJob(job);
        DelayJob delayJob = new DelayJob(job);
        delayBucket.addDelayJob(delayJob);
        return delayJob;
    }

    /**
     * 获取
     */
    public Job<?> getProcessJob(String topic) {
        // 拿到任务
        DelayJob delayJob = readyQueue.popJob(topic);
        if (delayJob == null || StrUtil.isBlank(delayJob.getJodId())) {
            return new Job<>();
        }
        Job<?> job = jobPool.getJob(delayJob.getJodId());
        // 元数据已经删除，则取下一个
        if (job == null) {
            job = getProcessJob(topic);
        } else {
            job.setStatus(JobStatus.RESERVED);
            delayJob.setDelayDate(System.currentTimeMillis() + job.getTtrTime());

            jobPool.addJob(job);
            delayBucket.addDelayJob(delayJob);
        }
        return job;
    }

    /**
     * 完成一个执行的任务
     */
    public void finishJob(String jobId) {
        jobPool.removeDelayJob(jobId);
    }

    /**
     * 伤处一个执行的任务
     */
    public void deleteJob(String jobId) {
        jobPool.removeDelayJob(jobId);
    }

}
