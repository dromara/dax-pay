package cn.daxpay.open.platform.capability.cache.core;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/// # 本地缓存注册表
///
/// 管理各个 cacheName 对应的本地缓存实例。
///
/// 设计要点：
/// - 每个 cacheName 独立一个本地缓存空间，避免 key 冲突
/// - 本地缓存 key 必须是字符串类型，与 RocketMQ 广播消息保持一致
/// - 本地缓存具有短 TTL，作为消息丢失或延迟时的兜底机制
@Slf4j
public class LocalCacheRegistry {

    private final Map<String, Cache<Object, Object>> localCaches = new ConcurrentHashMap<>();

    private final long defaultTtlSeconds;

    private final long defaultMaximumSize;

    public LocalCacheRegistry(long defaultTtlSeconds, long defaultMaximumSize) {
        this.defaultTtlSeconds = defaultTtlSeconds;
        this.defaultMaximumSize = defaultMaximumSize;
    }

    /// 获取或创建本地缓存实例
    ///
    /// @param cacheName 缓存名称
    /// @return 本地缓存实例
    public Cache<Object, Object> getOrCreate(String cacheName) {
        return this.localCaches.computeIfAbsent(cacheName, name -> createCache(name, this.defaultTtlSeconds, this.defaultMaximumSize));
    }

    /// 删除指定缓存键
    ///
    /// 注意：key 必须是标准化后的字符串 key，与本地缓存存储时使用的 key 一致
    ///
    /// @param cacheName 缓存名称
    /// @param key       标准化后的字符串缓存键
    public void evict(String cacheName, String key) {
        Cache<Object, Object> cache = this.localCaches.get(cacheName);
        if (cache != null) {
            cache.invalidate(key);
            log.debug("本地缓存删除成功: cacheName={}, key={}", cacheName, key);
        }
    }

    /// 清空指定缓存
    ///
    /// @param cacheName 缓存名称
    public void clear(String cacheName) {
        Cache<Object, Object> cache = this.localCaches.get(cacheName);
        if (cache != null) {
            cache.invalidateAll();
            log.debug("本地缓存清空成功: cacheName={}", cacheName);
        }
    }

    /// 创建本地缓存
    private Cache<Object, Object> createCache(String cacheName, long ttlSeconds, long maximumSize) {
        Caffeine<Object, Object> builder = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofSeconds(ttlSeconds))
                .maximumSize(maximumSize)
                .recordStats();
        log.debug("创建本地缓存: cacheName={}, ttl={}s, maximumSize={}", cacheName, ttlSeconds, maximumSize);
        return builder.build();
    }
}

