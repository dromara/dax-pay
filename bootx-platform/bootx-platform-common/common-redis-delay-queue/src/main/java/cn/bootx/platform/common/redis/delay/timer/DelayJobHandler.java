package cn.bootx.platform.common.redis.delay.timer;

import cn.bootx.platform.common.redis.delay.bean.DelayJob;
import cn.bootx.platform.common.redis.delay.bean.Job;
import cn.bootx.platform.common.redis.delay.configuration.DelayQueueProperties;
import cn.bootx.platform.common.redis.delay.constants.JobStatus;
import cn.bootx.platform.common.redis.delay.container.*;
import cn.hutool.core.thread.ThreadUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.extern.slf4j.Slf4j;

/**
 * 延时任务处理
 *
 * @param index 索引
 * @author daify
 * @date 2019-08-08 14:28
 */
@Slf4j
public record DelayJobHandler(DelayBucket delayBucket,
                              DelayJobPool delayJobPool,
                              DelayTopic delayTopic,
                              DelayQueue delayQueue,
                              DelayQueueProperties delayQueueProperties,
                              LockTemplate lockTemplate, int index) implements Runnable {

    /**
     * 轮训任务
     */
    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        while (true) {
            try {
                // 从存储桶中获取延时任务
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
                Job<?> job = delayJobPool.getJob(delayJob.getJodId());
                //延迟任务元数据不存在
                if (job == null) {
                    log.debug("移除结束的任务:{}", delayJob.getJodId());
                    delayBucket.removeDelayTime(index, delayJob);
                    continue;
                }
                // 任务超时
                JobStatus status = job.getStatus();
                if (JobStatus.RESERVED.equals(status)) {
                    log.debug("超时任务处理:{}", job.getId());
                    // 超时任务处理, 重新投递到延时队列
                    this.processTtrJob(delayJob, job);
                }  else if (JobStatus.DELAY.equals(status)){
                    // 延时任务处理, 写入延时队列
                    log.debug("延时任务处理:{}", job.getId());
                    this.processDelayJob(delayJob, job);
                }
            } catch (Exception e) {
                log.error("扫描DelayBucket出错：", e);
                ThreadUtil.sleep(delayQueueProperties.getSleepTime());
            }
        }
    }

    /**
     * 处理超时任务, 超时后如果重试未超过重试次数则重新投递,
     */
    private void processTtrJob(DelayJob delayJob, Job<?> job) {
        // 加锁
        LockInfo lock = lockTemplate.lock("lock:processTtrJob:"+job.getId());
        if (lock == null){
            return;
        }
        try {
            // 如果重试次数未超过最大次数进行重新投递, 超过后则直接设置为死亡
            if (job.getRetryCount() < delayQueueProperties.getRetryCount()){
                // 修改任务池状态
                delayJobPool.addOrUpdateJob(job);
                // 移除delayBucket中的任务
                delayBucket.removeDelayTime(index, delayJob);
                // 设置指定时间后重新投递, 当前时间 + 指定延后推送时间
                long delayDate = System.currentTimeMillis() + delayQueueProperties.getRetryTime();
                delayJob.setDelayDate(delayDate);
                // 再次添加到delayBucket中
                delayBucket.addDelayJob(delayJob);
                job.setRetryCount(job.getRetryCount() + 1)
                        .setStatus(JobStatus.DELAY);
                delayJobPool.addOrUpdateJob(job);
            } else {
                log.debug("任务处理失败, 移入死信队列:{}", job.getId());
                job.setStatus(JobStatus.DEAD);
                delayJobPool.removeJob(job.getId());
                delayJobPool.addOrUpdateDeadJob(job);
                // 写入死信队列
                delayTopic.incrementDead(job.getTopic());
                delayQueue.pushDeadJob(delayJob);
            }
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 处理延时任务, 写入就绪队列
     */
    private void processDelayJob(DelayJob delayJob, Job<?> job) {
        // 加锁
        LockInfo lock = lockTemplate.lock("lock:processDelayJob:"+job.getId());
        if (lock == null){
            return;
        }
        try {
            // 修改任务池状态
            job.setStatus(JobStatus.READY);
            delayJobPool.addOrUpdateJob(job);
            // 设置到待处理任务
            delayQueue.pushJob(delayJob);
            // topic中待处理数量自增+1
            delayTopic.increment(job.getTopic());
            // 移除delayBucket中的任务
            delayBucket.removeDelayTime(index, delayJob);
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }
}
