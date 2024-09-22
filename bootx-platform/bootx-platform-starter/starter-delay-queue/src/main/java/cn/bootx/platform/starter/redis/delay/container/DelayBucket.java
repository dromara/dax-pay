package cn.bootx.platform.starter.redis.delay.container;

import cn.bootx.platform.starter.redis.delay.bean.QueueJob;
import cn.bootx.platform.starter.redis.delay.configuration.DelayQueueProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * 延时处理队列
 * @author daify
 * @date 2019-07-26 14:41
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class DelayBucket {

    private final RedisTemplate<String, QueueJob> redisTemplate;

    private static final AtomicInteger index = new AtomicInteger(0);

    private final DelayQueueProperties delayQueueProperties;

    @Getter
    private final List <String> bucketNames = new ArrayList <>();

    /**
     * 创建延时队列桶
     */
    @Bean
    public List<String> createBuckets() {
        IntStream.range(0, delayQueueProperties.getBucketCount())
                .mapToObj(i -> "delay:queue:bucket:" + i)
                .forEach(bucketNames::add);
        return bucketNames;
    }

    /**
     * 获得桶的名称
     */
    private String getThisBucketName() {
        int thisIndex = index.addAndGet(1);
        int i1 = thisIndex % delayQueueProperties.getBucketCount();
        return bucketNames.get(i1);
    }

    /**
     * 获得桶集合
     */
    public BoundZSetOperations<String, QueueJob> getBucket(String bucketName) {
        return redisTemplate.boundZSetOps(bucketName);
    }

    /**
     * 放入延时任务
     */
    public void addDelayJob(QueueJob job) {
        String thisBucketName = getThisBucketName();
        var bucket = this.getBucket(thisBucketName);
        bucket.add(job,job.getDelayDate());
    }

    /**
     * 获得最新的延期任务
     */
    public QueueJob getFirstDelayTime(Integer index) {
        String name = bucketNames.get(index);
        var bucket = getBucket(name);
        var set = bucket.rangeWithScores(0, 1);
        if (CollectionUtils.isEmpty(set)) {
            return null;
        }
        var typedTuple = new ArrayList<>(set).getFirst();
        return typedTuple.getValue();
    }

    /**
     * 移除延时任务
     */
    public void removeDelayTime(Integer index, QueueJob queueJob) {
        String name = bucketNames.get(index);
        var bucket = getBucket(name);
        bucket.remove(queueJob);
    }

}
