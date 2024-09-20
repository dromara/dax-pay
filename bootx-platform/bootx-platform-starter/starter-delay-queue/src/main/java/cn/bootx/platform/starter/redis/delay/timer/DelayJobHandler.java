package cn.bootx.platform.starter.redis.delay.timer;

import cn.bootx.platform.starter.redis.delay.bean.QueueJob;
import cn.bootx.platform.starter.redis.delay.bean.DelayJob;
import cn.bootx.platform.starter.redis.delay.configuration.DelayQueueProperties;
import cn.bootx.platform.starter.redis.delay.constants.JobStatus;
import cn.bootx.platform.starter.redis.delay.container.DelayBucket;
import cn.bootx.platform.starter.redis.delay.container.DelayJobPool;
import cn.bootx.platform.starter.redis.delay.container.DelayQueue;
import cn.bootx.platform.starter.redis.delay.container.DelayTopic;
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
                QueueJob queueJob = delayBucket.getFirstDelayTime(index);
                //没有任务
                if (queueJob == null) {
                    ThreadUtil.sleep(delayQueueProperties.getSleepTime());
                    continue;
                }
                // 延迟时间没到
                if (queueJob.getDelayDate() > System.currentTimeMillis()) {
                    ThreadUtil.sleep(delayQueueProperties.getSleepTime());
                    continue;
                }
                DelayJob<?> delayJob = delayJobPool.getJob(queueJob.getJodId());
                //延迟任务元数据不存在
                if (delayJob == null) {
                    log.debug("移除结束的任务:{}", queueJob.getJodId());
                    delayBucket.removeDelayTime(index, queueJob);
                    continue;
                }
                // 任务超时
                JobStatus status = delayJob.getStatus();
                if (JobStatus.RESERVED.equals(status)) {
                    log.debug("超时任务处理:{}", delayJob.getId());
                    // 超时任务处理, 重新投递到延时队列
                    this.processTtrJob(queueJob, delayJob);
                }  else if (JobStatus.DELAY.equals(status)){
                    // 延时任务处理, 写入延时队列
                    log.debug("延时任务处理:{}", delayJob.getId());
                    this.processDelayJob(queueJob, delayJob);
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
    private void processTtrJob(QueueJob queueJob, DelayJob<?> delayJob) {
        // 加锁
        LockInfo lock = lockTemplate.lock("lock:processTtrJob:"+ delayJob.getId());
        if (lock == null){
            return;
        }
        try {
            // 如果重试次数未超过最大次数进行重新投递, 超过后则直接设置为死亡
            if (delayJob.getRetryCount() < delayQueueProperties.getRetryCount()){
                // 修改任务池状态
                delayJobPool.addOrUpdateJob(delayJob);
                // 移除delayBucket中的任务
                delayBucket.removeDelayTime(index, queueJob);
                // 设置指定时间后重新投递, 当前时间 + 指定延后推送时间
                long delayDate = System.currentTimeMillis() + delayQueueProperties.getRetryTime();
                queueJob.setDelayDate(delayDate);
                // 再次添加到delayBucket中
                delayBucket.addDelayJob(queueJob);
                delayJob.setRetryCount(delayJob.getRetryCount() + 1)
                        .setStatus(JobStatus.DELAY);
                delayJobPool.addOrUpdateJob(delayJob);
            } else {
                log.debug("任务处理失败, 移入死信队列:{}", delayJob.getId());
                delayJob.setStatus(JobStatus.DEAD);
                delayJobPool.removeJob(delayJob.getId());
                delayJobPool.addOrUpdateDeadJob(delayJob);
                // 写入死信队列
                delayTopic.incrementDead(delayJob.getTopic());
                delayQueue.pushDeadJob(queueJob);
            }
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 处理延时任务, 写入就绪队列
     */
    private void processDelayJob(QueueJob queueJob, DelayJob<?> delayJob) {
        // 加锁
        LockInfo lock = lockTemplate.lock("lock:processDelayJob:"+ delayJob.getId());
        if (lock == null){
            return;
        }
        try {
            // 修改任务池状态
            delayJob.setStatus(JobStatus.READY);
            delayJobPool.addOrUpdateJob(delayJob);
            // 设置到待处理任务
            delayQueue.pushJob(queueJob);
            // topic中待处理数量自增+1
            delayTopic.increment(delayJob.getTopic());
            // 移除delayBucket中的任务
            delayBucket.removeDelayTime(index, queueJob);
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }
}
