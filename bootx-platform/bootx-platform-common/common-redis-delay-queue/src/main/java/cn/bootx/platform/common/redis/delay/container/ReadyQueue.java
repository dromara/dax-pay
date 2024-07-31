package cn.bootx.platform.common.redis.delay.container;

import cn.bootx.platform.common.redis.delay.bean.DelayJob;
import cn.bootx.platform.core.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 *
 * @author daify
 * @date 2019-08-08 14:07
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class ReadyQueue {

    private final RedisTemplate redisTemplate;

    private String NAME = "process.queue";

    private String getKey(String topic) {
        return NAME + topic;
    }

    /**
     * 获得队列
     * @param topic
     * @return
     */
    private BoundListOperations getQueue (String topic) {
        BoundListOperations ops = redisTemplate.boundListOps(getKey(topic));
        return ops;
    }

    /**
     * 设置任务
     * @param delayJob
     */
    public void pushJob(DelayJob delayJob) {
        log.info("执行队列添加任务:{}",delayJob);
        BoundListOperations listOperations = getQueue(delayJob.getTopic());
        listOperations.leftPush(delayJob);
    }

    /**
     * 移除并获得任务
     * @param topic
     * @return
     */
    public DelayJob popJob(String topic) {
        BoundListOperations listOperations = getQueue(topic);
        Object o = listOperations.leftPop();
        if (o instanceof DelayJob job) {
            log.info("执行队列取出任务:{}", JsonUtil.toJsonStr(job));
            return job;
        }
        return null;
    }

}
