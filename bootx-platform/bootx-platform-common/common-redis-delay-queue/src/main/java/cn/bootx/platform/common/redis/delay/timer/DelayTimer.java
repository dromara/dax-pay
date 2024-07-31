package cn.bootx.platform.common.redis.delay.timer;

import cn.bootx.platform.common.redis.delay.container.DelayBucket;
import cn.bootx.platform.common.redis.delay.container.JobPool;
import cn.bootx.platform.common.redis.delay.container.ReadyQueue;
import cn.bootx.platform.common.redis.delay.handler.DelayJobHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author daify
 * @date 2019-08-08 14:15
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class DelayTimer implements ApplicationListener <ContextRefreshedEvent> {

    private final DelayBucket delayBucket;
    private final JobPool jobPool;
    private final ReadyQueue readyQueue;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        int length = 1;
        ExecutorService executorService = new ThreadPoolExecutor(
                length,
                length,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());

        for (int i = 0; i < length; i++) {
            executorService.execute(
                    new DelayJobHandler(
                            delayBucket,
                            jobPool,
                            readyQueue,
                            i));
        }

    }
}
