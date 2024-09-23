package cn.bootx.platform.starter.redis.delay.service;

import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.starter.redis.delay.bean.DelayJob;
import cn.bootx.platform.starter.redis.delay.bean.QueueJob;
import cn.bootx.platform.starter.redis.delay.container.DelayBucket;
import cn.bootx.platform.starter.redis.delay.container.DelayJobPool;
import cn.bootx.platform.starter.redis.delay.container.DelayQueue;
import cn.bootx.platform.starter.redis.delay.container.DelayTopic;
import cn.bootx.platform.starter.redis.delay.result.BucketResult;
import cn.bootx.platform.starter.redis.delay.result.DelayJobResult;
import cn.bootx.platform.starter.redis.delay.result.TopicResult;
import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 * 延时队列管理
 * @author xxm
 * @since 2024/9/21
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DelayQueueService {
    private final DelayBucket delayBucket;
    private final DelayQueue delayQueue;
    private final DelayTopic delayTopic;
    private final DelayJobPool delayJobPool;

    /**
     * 获取桶信息列表
     */
    public List<BucketResult> getBucket(){
        return delayBucket.getBucketNames().stream()
                .map(bucketName -> {
                    BoundZSetOperations<String, QueueJob> bucket = delayBucket.getBucket(bucketName);
                    return new BucketResult().setName(bucketName).setCount(Optional.ofNullable(bucket).map(BoundZSetOperations::size).map(Long::intValue).orElse(0));
                }).toList();
    }

    /**
     * 获取延时桶中待执行的任务
     */
    public PageResult<DelayJobResult> pageBucketJob(String bucketName, PageParam pageParam){
        BoundZSetOperations<String, QueueJob> bucket = delayBucket.getBucket(bucketName);
        Long total = Optional.ofNullable(bucket.size()).orElse(0L);
        List<DelayJobResult> list = Optional.ofNullable(bucket.range(pageParam.start(), pageParam.end()))
                .map(ArrayList::new)
                .orElse(new ArrayList<>())
                .stream()
                .map(queueJob -> new DelayJobResult()
                        .setId(queueJob.getJodId())
                        .setTopic(queueJob.getTopic())
                        .setDelayTime(LocalDateTimeUtil.of(queueJob.getDelayDate())))
                .toList();
        return new PageResult<DelayJobResult>().setCurrent(pageParam.getCurrent()).setRecords(list).setSize(pageParam.getSize()).setTotal(total);

    }

    /**
     * 获取就绪主题数量和列表
     */
    public List<TopicResult> getDelayTopic(){
        BoundZSetOperations<String, String> pool = delayTopic.getPool();
        return Optional.ofNullable(pool.rangeWithScores(0, Long.MAX_VALUE)).orElse(new HashSet<>()).stream()
                .map(rangeWithScore -> new TopicResult().setName(rangeWithScore.getValue()).setCount(Optional.ofNullable(rangeWithScore.getScore()).map(Double::intValue).orElse(0)))
                .toList();
    }

    /**
     * 获取就绪主题任务分页
     */
    public PageResult<DelayJobResult> pageReadyJob(String topic, PageParam pageParam){
        BoundListOperations<String, QueueJob> queue = delayQueue.getQueue(topic);
        Long total = Optional.ofNullable(queue.size()).orElse(0L);
        List<DelayJobResult> list = Optional.ofNullable(queue.range(pageParam.start(), pageParam.end()))
                .orElse(new ArrayList<>())
                .stream()
                .map(queueJob -> new DelayJobResult()
                        .setId(queueJob.getJodId())
                        .setTopic(queueJob.getTopic())
                        .setDelayTime(LocalDateTimeUtil.of(queueJob.getDelayDate())))
                .toList();
        return new PageResult<DelayJobResult>().setCurrent(pageParam.getCurrent()).setRecords(list).setSize(pageParam.getSize()).setTotal(total);
    }


    /**
     * 获取任务详情
     */
    public DelayJobResult getJobDetail(String jobId){
        DelayJob<?> deadJob = delayJobPool.getDeadJob(jobId);
        return DelayJobResult.init(deadJob);
    }

    /**
     * 获取死信队列主题数量和列表
     */
    public List<TopicResult> getDeadTopic(){
        BoundZSetOperations<String, String> pool = delayTopic.getDeadPool();
        return Optional.ofNullable(pool.rangeWithScores(0, Long.MAX_VALUE)).orElse(new HashSet<>()).stream()
                .map(rangeWithScore -> new TopicResult().setName(rangeWithScore.getValue()).setCount(Optional.ofNullable(rangeWithScore.getScore()).map(Double::intValue).orElse(0))).toList();
    }


    /**
     * 获取死信主题任务分页
     */
    public PageResult<DelayJobResult> pageDeadJob(String topic, PageParam pageParam){
        BoundListOperations<String, QueueJob> queue = delayQueue.getDeadQueue(topic);
        Long total = Optional.ofNullable(queue.size()).orElse(0L);
        List<DelayJobResult> list = Optional.ofNullable(queue.range(pageParam.start(), pageParam.end()))
                .orElse(new ArrayList<>())
                .stream()
                .map(queueJob -> new DelayJobResult()
                        .setId(queueJob.getJodId())
                        .setTopic(queueJob.getTopic())
                        .setDelayTime(LocalDateTimeUtil.of(queueJob.getDelayDate())))
                .toList();
        return new PageResult<DelayJobResult>().setCurrent(pageParam.getCurrent()).setRecords(list).setSize(pageParam.getSize()).setTotal(total);
    }

    /**
     * 获取死信任务详情
     */
    public DelayJobResult getDeadJobDetail(String jobId){
        DelayJob<?> deadJob = delayJobPool.getDeadJob(jobId);
        return DelayJobResult.init(deadJob);
    }

    /**
     * 重新投递
     */
    public void resetDeadJob(QueueJob queueJob){
    }

    /**
     * 移除死信任务
     */
    public void removeDeadJob(String jobId){
    }

}
