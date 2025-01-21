package cn.bootx.platform.starter.redis.delay.container;

import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 就绪任务主题数量表
 * @author xxm
 * @since 2024/8/1
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DelayTopic {

    private final RedisTemplate<String, String> redisTemplate;


    /**
     * 获得zset集合
     */
    public BoundZSetOperations<String, String> getPool() {
        String name = "delay:queue:topics";
        return redisTemplate.boundZSetOps(name);
    }

    /**
     * 放入延时任务
     */
    public void increment(String topic) {
        var bucket = this.getPool();
        if (bucket.score(topic) == null){
            bucket.addIfAbsent(topic,0);
        }
        bucket.incrementScore(topic,1);
    }

    /**
     * 自减
     */
    public void decrement(String topic){
        var pool = this.getPool();
        if (pool.score(topic) != null){
            //noinspection DataFlowIssue
            double v = pool.incrementScore(topic, -1);
            if (v <= 0){
                pool.remove(topic);
            }
        }
    }

    /**
     * 获取全部, 数量大于1的主题
     */
    public List<String> getAll(){
        var pool = this.getPool();
        Set<String> range = pool.range(0, Long.MAX_VALUE);
        if (CollUtil.isNotEmpty(range)){
            return new ArrayList<>(range);
        }
        return new ArrayList<>();
    }


    /**
     * 获得死信zset集合
     */
    public BoundZSetOperations<String, String> getDeadPool() {
        String name = "delay:queue:dead:topics";
        return redisTemplate.boundZSetOps(name);
    }

    /**
     * 放入延时任务
     */
    public void incrementDead(String topic) {
        var bucket = this.getDeadPool();
        if (bucket.score(topic) == null){
            bucket.addIfAbsent(topic,0);
        }
        bucket.incrementScore(topic,1);
    }

    /**
     * 自减
     */
    public void decrementDead(String topic){
        var pool = this.getDeadPool();
        if (pool.score(topic) != null){
            //noinspection DataFlowIssue
            double v = pool.incrementScore(topic, -1);
            if (v <= 0){
                pool.remove(topic);
            }
        }
    }

    /**
     * 获取全部, 数量大于1的主题
     */
    public List<String> deadGetAll(){
        var pool = this.getDeadPool();
        Set<String> range = pool.range(0, Long.MAX_VALUE);
        if (CollUtil.isNotEmpty(range)){
            return new ArrayList<>(range);
        }
        return new ArrayList<>();
    }
}
