package cn.daxpay.open.platform.capability.cache.core;

import cn.daxpay.open.platform.capability.cache.notify.publisher.CacheInvalidationPublisher;
import cn.daxpay.open.platform.capability.cache.secure.SecureCacheNameMatcher;
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
/// - 两层开关：[#cacheEnabled] 总开关（关=L1+L2 全关）；[#l1Enabled] L1 单独开关（总开关开时可关 L1 留 L2）
/// - L2 无单独开关：关 L2 等同于关总开关
/// - 缓存失效通知通过 Artemis 广播
/// - 敏感 cacheName 结合 secureL2Enabled 决定是否写 Redis
@Slf4j
@RequiredArgsConstructor
public class MultiLevelCacheManager implements CacheManager {

    private final CacheManager redisCacheManager;

    private final LocalCacheRegistry localCacheRegistry;

    private final CacheInvalidationPublisher publisher;

    private final SecureCacheNameMatcher secureMatcher;

    /// 敏感缓存是否允许 L2（数据加密已启用时为 true）
    private final boolean secureL2Enabled;

    /// 缓存总开关，false 时 L1+L2 一并 NoOp（直接穿透到方法）
    ///
    /// 由 [cn.daxpay.open.platform.common.config.properties.PlatformCommonProperties.Cache#isEnabled] 透传。
    private final boolean cacheEnabled;

    /// L1 本地缓存单独开关，false 时跳过 L1 仅保留 L2 Redis
    ///
    /// 由 [cn.daxpay.open.platform.common.config.properties.PlatformCommonProperties.Cache.L1#isEnabled] 透传。
    /// 仅在 [#cacheEnabled] 为 true 时有意义。
    private final boolean l1Enabled;

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

        boolean secureCache = this.secureMatcher != null && this.secureMatcher.matches(name);
        log.debug("创建二级缓存: name={}, secure={}, secureL2Enabled={}, cacheEnabled={}, l1Enabled={}",
                name, secureCache, this.secureL2Enabled, this.cacheEnabled, this.l1Enabled);
        return new MultiLevelCache(
                name,
                this.localCacheRegistry,
                redisCache,
                this.publisher,
                secureCache,
                this.secureL2Enabled,
                this.cacheEnabled,
                this.l1Enabled
        );
    }
}
