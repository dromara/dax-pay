package cn.bootx.platform.common.redis.delay.handler;

import cn.bootx.platform.common.redis.delay.annotation.DelayTaskJobBeanPostProcessor;
import cn.bootx.platform.common.redis.delay.bean.DelayJob;
import cn.bootx.platform.common.redis.delay.bean.Job;
import cn.bootx.platform.common.redis.delay.constants.DelayConfig;
import cn.bootx.platform.common.redis.delay.constants.JobStatus;
import cn.bootx.platform.common.redis.delay.container.DelayBucket;
import cn.bootx.platform.common.redis.delay.container.JobPool;
import cn.bootx.platform.common.redis.delay.container.ReadyQueue;
import cn.bootx.platform.core.util.JsonUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author daify
 * @date 2019-08-08 14:28
 **/
@Slf4j
@Data
@AllArgsConstructor
public class DelayJobHandler implements Runnable{

    /**
     * 延迟队列
     */
    private DelayBucket delayBucket;
    /**
     * 任务池
     */
    private JobPool jobPool;

    /**
     * 就绪队列
     */
    private ReadyQueue readyQueue;
    /**
     * 索引
     */
    private int index;

    /**
     */
    @Override
    public void run() {
        log.info("定时任务开始执行");
        while (true) {
            try {
                DelayJob delayJob = delayBucket.getFirstDelayTime(index);
                //没有任务
                if (delayJob == null) {
                    sleep();
                    continue;
                }
                // 发现延时任务
                // 延迟时间没到
                if (delayJob.getDelayDate() > System.currentTimeMillis()) {
                    sleep();
                    continue;
                }
                Job job = jobPool.getJob(delayJob.getJodId());

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
                    DelayTaskJobBeanPostProcessor bean = SpringUtil.getBean(DelayTaskJobBeanPostProcessor.class);
                    bean.run(delayJob.getTopic(), job.getMessage());
                    processDelayJob(delayJob,job);
                }
            } catch (Exception e) {
                log.error("扫描DelayBucket出错：",e);
                sleep();
            }
        }
    }

    /**
     * 处理ttr的任务
     */
    private void processTtrJob(DelayJob delayJob,Job job) {
        job.setStatus(JobStatus.DELAY);
        // 修改任务池状态
        jobPool.addJob(job);
        // 移除delayBucket中的任务
        delayBucket.removeDelayTime(index,delayJob);
        Long delayDate = System.currentTimeMillis() + job.getDelayTime();
        delayJob.setDelayDate(delayDate);
        // 再次添加到任务中
        delayBucket.addDelayJob(delayJob);
    }

    /**
     * 处理延时任务
     */
    private void processDelayJob(DelayJob delayJob,Job job) {
        job.setStatus(JobStatus.READY);
        // 修改任务池状态
        jobPool.addJob(job);
        // 设置到待处理任务
        readyQueue.pushJob(delayJob);
        // 移除delayBucket中的任务
        delayBucket.removeDelayTime(index,delayJob);
    }

    private void sleep(){
        try {
            Thread.sleep(DelayConfig.SLEEP_TIME);
        } catch (InterruptedException e){
            log.error("",e);
        }
    }
}
