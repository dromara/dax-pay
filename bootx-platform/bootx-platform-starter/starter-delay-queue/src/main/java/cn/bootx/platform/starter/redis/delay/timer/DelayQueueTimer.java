package cn.bootx.platform.starter.redis.delay.timer;

import cn.bootx.platform.starter.redis.delay.annotation.DelayJobProcessor;
import cn.bootx.platform.starter.redis.delay.bean.DelayJob;
import cn.bootx.platform.starter.redis.delay.configuration.DelayQueueProperties;
import cn.bootx.platform.starter.redis.delay.container.DelayBucket;
import cn.bootx.platform.starter.redis.delay.container.DelayJobPool;
import cn.bootx.platform.starter.redis.delay.container.DelayQueue;
import cn.bootx.platform.starter.redis.delay.container.DelayTopic;
import cn.bootx.platform.starter.redis.delay.service.DelayJobService;
import cn.bootx.platform.core.exception.UnSupportOperateException;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * 延时队列
 * @author daify
 * @date 2019-07-26 15:12
 **/
@Slf4j
@Configuration
@RequiredArgsConstructor
public class DelayQueueTimer implements ApplicationListener<ContextRefreshedEvent> {

    private final DelayBucket delayBucket;
    private final DelayJobPool delayJobPool;
    private final DelayTopic delayTopic;
    private final DelayQueue delayQueue;
    private final DelayQueueProperties delayQueueProperties;
    private final DelayJobService delayJobService;
    private final DelayJobProcessor delayJobProcessor;
    private final LockTemplate lockTemplate;


    /**
     * 启动轮训定时任务, 将任务投递到延时队列中
     */
    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent contextRefreshedEvent) {
        int length = delayQueueProperties.getBucketCount();
        // 创建线程池并执行
        ExecutorService executorService = new ThreadPoolExecutor(length, length, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        IntStream.range(0, length)
                .mapToObj(i -> new DelayJobHandler(
                        delayBucket,
                        delayJobPool,
                        delayTopic,
                        delayQueue,
                        delayQueueProperties,
                        lockTemplate,
                        i))
                .forEachOrdered(executorService::execute);
        log.info("启动延时队列完成");
    }

    /**
     * 消费延时队列的任务, 使用Spring的定时任务, 一秒执行一次, 如果有结果就一直执行
     */
    @Async
    @Scheduled(fixedRate = 1000)
    public void consumeDelayQueue() {
        // 读取topic 信息表, 获取数量不为空的主题名称
        for (String topic : delayTopic.getAll()) {
            // 只处理订阅的主题
            if (!delayJobProcessor.existTopic(topic)){
               continue;
            }
            DelayJob<?> processDelayJob = delayJobService.getProcessJob(topic);
            while (processDelayJob != null){
                try {
                    delayJobProcessor.invoke(processDelayJob.getTopic(), processDelayJob);
                    delayJobService.finishJob(processDelayJob);
                } catch (InvocationTargetException | IllegalAccessException | UnSupportOperateException e) {
                    log.warn("消息消费失败! ",e);
                }
                delayTopic.decrement(processDelayJob.getTopic());
                // 如果有任务就继续进行执行
                processDelayJob = delayJobService.getProcessJob(topic);
            }
        }
    }
}
