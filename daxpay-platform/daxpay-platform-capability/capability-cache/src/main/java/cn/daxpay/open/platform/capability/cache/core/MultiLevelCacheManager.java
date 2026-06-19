package cn.daxpay.open.platform.capability.cache.core;

import cn.daxpay.open.platform.capability.cache.notify.publisher.CacheInvalidationPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/// # 二级缓存管理器
///
/// 管理 L1 本地缓存和 L2 Redis 缓存。
///
/// 设计要点：
/// - 作为 Spring 默认 CacheManager，通过 @Primary 标识
/// - L1 本地缓存始终启用，不需要开关控制
/// - 缓存失效通知通过 RocketMQ 广播，始终启用
@Slf4j
@RequiredArgsConstructor
public class MultiLevelCacheManager implements CacheManager {

    private final CacheManager redisCacheManager;

    private final LocalCacheRegistry localCacheRegistry;

    private final CacheInvalidationPublisher publisher;

    private final Map<String, MultiLevelCache> cacheMap = new ConcurrentHashMap<>();

    @Override
    public Cache getCache(String name) {
        return this.cacheMap.computeIfAbsent(name, this::createCache);
    }

    @Override
    public Collection<String> getCacheNames() {
        return Collections.unmodifiableSet(this.cacheMap.keySet());
    }

    /// 创建二级缓存实例
    ///
    /// 每个缓存名称对应一个独立的二级缓存实例，包含独立的 L1 本地缓存空间
    private MultiLevelCache createCache(String name) {
        Cache redisCache = this.redisCacheManager.getCache(name);
        if (redisCache == null) {
            throw new IllegalStateException("Redis cache not found: " + name);
        }

        log.debug("创建二级缓存: name={}", name);
        return new MultiLevelCache(
                name,
                this.localCacheRegistry,
                redisCache,
                this.publisher
        );
    }
}


