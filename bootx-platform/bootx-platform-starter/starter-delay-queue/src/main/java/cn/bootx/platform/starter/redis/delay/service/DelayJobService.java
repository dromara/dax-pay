package cn.bootx.platform.starter.redis.delay.service;

import cn.bootx.platform.starter.redis.delay.bean.DelayJob;
import cn.bootx.platform.starter.redis.delay.bean.QueueJob;
import cn.bootx.platform.starter.redis.delay.constants.JobStatus;
import cn.bootx.platform.starter.redis.delay.container.DelayBucket;
import cn.bootx.platform.starter.redis.delay.container.DelayJobPool;
import cn.bootx.platform.starter.redis.delay.container.DelayQueue;
import cn.bootx.platform.core.util.DateTimeUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;

/**
 * 延时任务服务类
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
     * 注册任务
     * @param obj 任务对象
     * @param topic 主题队列
     * @param delayTime 延迟时间(毫秒), 如果小于0则默认为0
     */
    public void register(Object obj, String topic, long delayTime) {
        if (delayTime < 0){
            delayTime = 0;
        }
        DelayJob<Object> delayJob = new DelayJob<>()
                .setId(IdUtil.getSnowflakeNextIdStr())
                .setDelayTime(delayTime)
                .setStatus(JobStatus.DELAY)
                .setMessage(obj)
                .setTopic(topic);
        delayJobPool.addOrUpdateJob(delayJob);
        QueueJob queueJob = new QueueJob(delayJob);
        delayBucket.addDelayJob(queueJob);
    }

    /**
     * 注册任务
     */
    public void register(Object obj, String topic, LocalDateTime dateTime) {
        // 判断与当前相差的毫秒值
        long delayTime = DateTimeUtil.timestamp(dateTime) - DateTimeUtil.timestamp(LocalDateTime.now());
        this.register(obj, topic, delayTime);
    }

    /**
     * 注册任务, 如果处在事务中, 将在事务执行完成后进行发送, 失败则不进行发送
     */
    public void registerByTransaction(Object obj, String topic, long delayTime) {
        boolean isTransaction = TransactionSynchronizationManager.isActualTransactionActive();
        if (isTransaction) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    register(obj, topic, delayTime);
                }
            });
        } else {
            register(obj, topic, delayTime);
        }
    }

    /**
     * 注册任务, 如果处在事务中, 将在事务执行完成后进行发送, 失败则不进行发送
     */
    public void registerByTransaction(Object obj, String topic, LocalDateTime dateTime) {
        // 判断与当前相差的毫秒值
        long delayTime = DateTimeUtil.timestamp(dateTime) - DateTimeUtil.timestamp(LocalDateTime.now());
        this.registerByTransaction(obj, topic, delayTime);
    }

    /**
     * 获取任务
     */
    public DelayJob<?> getProcessJob(String topic) {
        // 从就绪队列拿到任务
        QueueJob queueJob = delayQueue.popJob(topic);
        if (queueJob == null || StrUtil.isBlank(queueJob.getJodId())) {
            return null;
        }
        DelayJob<?> delayJob = delayJobPool.getJob(queueJob.getJodId());
        // 元数据已经删除，则取下一个
        if (delayJob == null) {
            delayJob = getProcessJob(topic);
        } else {
            delayJob.setStatus(JobStatus.RESERVED);
            // 设置再次投递时间, 如果消费失败将会再次投递, 消费成功会删掉任务
            queueJob.setDelayDate(System.currentTimeMillis() + delayJob.getTtrTime());
            delayJobPool.addOrUpdateJob(delayJob);
            delayBucket.addDelayJob(queueJob);
        }
        return delayJob;
    }

    /**
     * 完成一个执行的任务
     */
    public void finishJob(DelayJob<?> delayJob) {
        delayJobPool.removeJob(delayJob.getId());
    }

    /**
     * 删除一个执行的任务
     */
    public void deleteJob(String jobId) {
        delayJobPool.removeJob(jobId);
    }

}
