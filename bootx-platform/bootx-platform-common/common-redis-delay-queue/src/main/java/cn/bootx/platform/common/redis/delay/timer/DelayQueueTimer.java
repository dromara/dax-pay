package cn.bootx.platform.common.redis.delay.timer;

import cn.bootx.platform.common.redis.delay.annotation.DelayTaskJobProcessor;
import cn.bootx.platform.common.redis.delay.configuration.DelayQueueProperties;
import cn.bootx.platform.common.redis.delay.container.DelayBucket;
import cn.bootx.platform.common.redis.delay.container.JobPool;
import cn.bootx.platform.common.redis.delay.container.ReadyQueue;
import cn.bootx.platform.common.redis.delay.handler.DelayJobHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 延时队列配置
 * @author daify
 * @date 2019-07-26 15:12
 **/
@Slf4j
@Configuration
@RequiredArgsConstructor
public class DelayQueueTimer implements ApplicationListener<ContextRefreshedEvent> {

    private final DelayBucket delayBucket;
    private final JobPool jobPool;
    private final ReadyQueue readyQueue;
    private final DelayQueueProperties delayQueueProperties;
    private final DelayTaskJobProcessor delayTaskJobProcessor;

    /**
     * 启动轮训定时任务
     */
    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent contextRefreshedEvent) {
        int length = delayQueueProperties.getBucketCount();
        // 创建线程池
        ExecutorService executorService = new ThreadPoolExecutor(length, length, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

        for (int i = 0; i < length; i++) {
            executorService.execute(
                    new DelayJobHandler(
                            delayBucket,
                            jobPool,
                            readyQueue,
                            delayQueueProperties,
                            delayTaskJobProcessor,
                            i));
        }
        log.info("启动延时队列完成");
    }
}
