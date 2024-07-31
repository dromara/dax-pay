package cn.bootx.platform.common.redis.delay.handler;

import cn.bootx.platform.common.redis.delay.annotation.DelayTaskJobProcessor;
import cn.bootx.platform.common.redis.delay.bean.DelayJob;
import cn.bootx.platform.common.redis.delay.bean.Job;
import cn.bootx.platform.common.redis.delay.configuration.DelayQueueProperties;
import cn.bootx.platform.common.redis.delay.constants.JobStatus;
import cn.bootx.platform.common.redis.delay.container.DelayBucket;
import cn.bootx.platform.common.redis.delay.container.JobPool;
import cn.bootx.platform.common.redis.delay.container.ReadyQueue;
import cn.bootx.platform.core.util.JsonUtil;
import cn.hutool.core.thread.ThreadUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;

/**
 * 延时任务处理
 * @author daify
 * @date 2019-08-08 14:28
 **/
@Slf4j
@Data
@RequiredArgsConstructor
public class DelayJobHandler implements Runnable{

    /**
     * 延迟队列
     */
    private final DelayBucket delayBucket;
    /**
     * 任务池
     */
    private final JobPool jobPool;

    /**
     * 就绪队列
     */
    private final ReadyQueue readyQueue;

    /**
     * 参数配置
     */
    private final DelayQueueProperties delayQueueProperties;

    /**
     * 延时任务处理方法
     */
    private final DelayTaskJobProcessor delayTaskJobProcessor;

    /**
     * 索引
     */
    private final int index;

    /**
     */
    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        while (true) {
            try {
                DelayJob delayJob = delayBucket.getFirstDelayTime(index);
                //没有任务
                if (delayJob == null) {
                    ThreadUtil.sleep(delayQueueProperties.getSleepTime());
                    continue;
                }
                // 延迟时间没到
                if (delayJob.getDelayDate() > System.currentTimeMillis()) {
                    ThreadUtil.sleep(delayQueueProperties.getSleepTime());
                    continue;
                }
                Job<?> job = jobPool.getJob(delayJob.getJodId());
                //延迟任务元数据不存在
                if (job == null) {
                    log.info("移除不存在任务:{}", JsonUtil.toJsonStr(delayJob));
                    delayBucket.removeDelayTime(index,delayJob);
                    continue;
                }
                JobStatus status = job.getStatus();
                if (JobStatus.RESERVED.equals(status)) {
                    log.info("处理超时任务:{}", JsonUtil.toJsonStr(job));
                    // 超时任务
                    processTtrJob(delayJob,job);
                } else {
                    log.info("处理延时任务:{}", JsonUtil.toJsonStr(job));
                    // 延时任务
                    processDelayJob(delayJob, job, delayTaskJobProcessor);
                }
            } catch (Exception e) {
                log.error("扫描DelayBucket出错：",e);
                ThreadUtil.sleep(delayQueueProperties.getSleepTime());
            }
        }
    }

    /**
     * 处理超时
     */
    private void processTtrJob(DelayJob delayJob,Job<?> job) {
        job.setStatus(JobStatus.DELAY);
        // 修改任务池状态
        jobPool.addJob(job);
        // 移除delayBucket中的任务
        delayBucket.removeDelayTime(index,delayJob);
        long delayDate = System.currentTimeMillis() + job.getDelayTime();
        delayJob.setDelayDate(delayDate);
        // 再次添加到任务中
        delayBucket.addDelayJob(delayJob);
    }

    /**
     * 处理延时任务
     */
    private void processDelayJob(DelayJob delayJob, Job<?> job, DelayTaskJobProcessor delayTaskJobProcessor) {
        try {
            job.setStatus(JobStatus.READY);
            // 修改任务池状态
            jobPool.addJob(job);
            // 设置到待处理任务
            readyQueue.pushJob(delayJob);
            delayTaskJobProcessor.run(job.getTopic(), job);
            // 移除delayBucket中的任务
            delayBucket.removeDelayTime(index,delayJob);
        } catch (InvocationTargetException | IllegalAccessException e) {
            log.error("处理延时任务失败", e);
        }
    }
}
