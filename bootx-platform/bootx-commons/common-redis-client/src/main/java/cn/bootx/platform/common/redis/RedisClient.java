package cn.bootx.platform.common.redis;

import cn.bootx.platform.common.redis.code.RedisCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis请求类
 *
 * @author xxm
 * @since 2020/4/9 15:34
 */
@RequiredArgsConstructor
public class RedisClient {

    private final StringRedisTemplate stringRedisTemplate;

    private final RedisTemplate<String, Object> redisTemplate;

    /** 删除key */
    public void deleteKey(String key) {
        stringRedisTemplate.delete(key);
    }

    /** 批量删除key */
    public void deleteKeys(Collection<String> keys) {
        stringRedisTemplate.delete(keys);
    }

    /** 设置值 */
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    /** 设置带超时时间的值 */
    public void setWithTimeout(String key, String value, long timeoutMs) {
        stringRedisTemplate.opsForValue().set(key, value, timeoutMs, TimeUnit.MILLISECONDS);
    }

    /** 设置超时通知的事件 */
    public void setKeyExpired(String keyPrefix, String key, long timeoutMs) {
        stringRedisTemplate.opsForValue().set(keyPrefix + key, "", timeoutMs, TimeUnit.MILLISECONDS);
    }

    /** 是否存在 */
    @SuppressWarnings("all")
    public boolean exists(String key) {
        return stringRedisTemplate.hasKey(key);
    }

    /** 获取值 */
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /** 批量获取值 */
    public List<String> get(Collection<String> keys) {
        return stringRedisTemplate.opsForValue().multiGet(keys);
    }

    /** hash设置值 */
    public void hset(String key, String column, Object value) {
        stringRedisTemplate.opsForHash().put(key, column, value);
    }

    /** hash获取值 */
    public Object hget(String key, String column) {
        return stringRedisTemplate.opsForHash().get(key, column);
    }

    /** 批量设置哈希值 */
    public void hmSet(String key, Map<String, String> map) {
        stringRedisTemplate.opsForHash().putAll(key, map);
    }

    /** 获取hash值 */
    public Map<String, String> hmGet(String key) {
        return stringRedisTemplate.<String, String>opsForHash().entries(key);
    }

    /** ZSet 添加 */
    @SuppressWarnings("all")
    public boolean zadd(String key, String value, long score) {
        return stringRedisTemplate.opsForZSet().add(key, value, score);
    }

    public Set<String> zrangeByScore(String key, long start, long end) {
        return stringRedisTemplate.opsForZSet().rangeByScore(key, start, end);
    }

    public void zremRangeByScore(String key, long start, long end) {
        stringRedisTemplate.opsForZSet().removeRangeByScore(key, start, end);
    }

    public void zremByMembers(String key, String... members) {
        stringRedisTemplate.opsForZSet().remove(key, members);
    }

    /** 设置超时 */
    public void expire(String key, long timeoutMs) {
        stringRedisTemplate.expire(key, timeoutMs, TimeUnit.MILLISECONDS);
    }

    /** 设置超时 */
    public void expireUnit(String key, long expire, TimeUnit timeUnit) {
        stringRedisTemplate.expire(key, expire, timeUnit);
    }

    /** 自增 */
    public Long increment(String key, long count) {
        return stringRedisTemplate.opsForValue().increment(key, count);
    }

    /** 重命名 */
    public void rename(String oldKey, String newKey) {
        stringRedisTemplate.boundSetOps(oldKey).rename(newKey);
    }

    /** 不存在进行赋值 */
    public Boolean setIfAbsent(String key, String value) {
        return stringRedisTemplate.opsForValue().setIfAbsent(key, value);
    }

    /** 不存在进行赋值,带超时 */
    public Boolean setIfAbsent(String key, String value, long timeoutMs) {
        return stringRedisTemplate.opsForValue().setIfAbsent(key, value, timeoutMs, TimeUnit.MILLISECONDS);
    }

    /** 获取原值并赋值 */
    public String getAndSet(String key, String value) {
        return stringRedisTemplate.opsForValue().getAndSet(key, value);
    }

    /** 发布订阅消息 */
    public void convertAndSend(String topic, Object message) {
        redisTemplate.convertAndSend(RedisCode.TOPIC_PREFIX + topic, message);
    }

}
