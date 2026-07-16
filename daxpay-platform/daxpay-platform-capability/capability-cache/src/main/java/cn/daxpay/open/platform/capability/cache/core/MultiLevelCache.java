package cn.daxpay.open.platform.capability.cache.core;

import cn.daxpay.open.platform.capability.cache.notify.publisher.CacheInvalidationPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;

import java.util.Objects;
import java.util.concurrent.Callable;

/// # 二级缓存实现
///
/// 组合 L1 本地缓存（Caffeine）和 L2 Redis 缓存，实现多级缓存架构。
///
/// 设计要点：
/// - L2 Redis 是共享缓存主层，负责跨节点数据共享
/// - L1 Caffeine 是性能加速层，仅在本节点生效
/// - 缓存失效通过 Artemis 广播通知其他节点删除本地 L1
/// - 本地缓存 key 必须统一使用字符串形式，保证跨节点广播删除一致性
/// - 敏感缓存（secure:）在数据加密未启用时仅使用 L1，禁止明文写 Redis
@Slf4j
public class MultiLevelCache implements Cache {

    private final String name;

    private final com.github.benmanes.caffeine.cache.Cache<Object, Object> localCache;

    private final Cache redisCache;

    private final CacheInvalidationPublisher publisher;

    /// 是否为敏感缓存名
    private final boolean secureCache;

    /// 敏感缓存是否允许写/读 L2（依赖数据加密已启用）
    private final boolean secureL2Enabled;

    public MultiLevelCache(String name,
                           LocalCacheRegistry localCacheRegistry,
                           Cache redisCache,
                           CacheInvalidationPublisher publisher,
                           boolean secureCache,
                           boolean secureL2Enabled) {
        this.name = name;
        this.localCache = localCacheRegistry.getOrCreate(name);
        this.redisCache = redisCache;
        this.publisher = publisher;
        this.secureCache = secureCache;
        this.secureL2Enabled = secureL2Enabled;
        if (secureCache && !secureL2Enabled) {
            log.warn("敏感缓存 [{}] 因未启用数据加密，仅使用 L1 本地缓存，不写 Redis", name);
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return this.localCache;
    }

    /// 是否跳过 L2（敏感且未开加密）
    private boolean isL1Only() {
        return this.secureCache && !this.secureL2Enabled;
    }

    /// 读取缓存，优先从 L1 本地缓存读取，未命中则从 L2 Redis 读取并回填 L1
    ///
    /// 读取流程：
    /// - 查 L1 本地缓存
    /// - L1 未命中则查 L2 Redis
    /// - L2 命中则回填 L1
    /// - 全未命中返回 null
    ///
    /// 敏感缓存且未启用加密时跳过 L2，仅查 L1
    @Override
    public ValueWrapper get(Object key) {
        String localKey = this.toLocalKey(key);
        Object localValue = this.localCache.getIfPresent(localKey);
        if (localValue != null) {
            return () -> localValue;
        }

        // 敏感缓存未开加密：禁止读 Redis，避免历史明文或无法解密的脏数据
        if (this.isL1Only()) {
            log.debug("敏感缓存 L1-only 未命中: cacheName={}, key={}", this.name, key);
            return null;
        }

        ValueWrapper redisValue = this.redisCache.get(key);
        if (redisValue != null) {
            this.localCache.put(localKey, Objects.requireNonNull(redisValue.get()));
            return redisValue;
        }

        log.debug("缓存未命中: cacheName={}, key={}", this.name, key);
        return null;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        ValueWrapper wrapper = this.get(key);
        if (wrapper != null) {
            Object value = wrapper.get();
            if (value != null && !type.isInstance(value)) {
                throw new IllegalStateException("Cached value is not of required type [" + type.getName() + "]: " + value);
            }
            return type.cast(value);
        }
        return null;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        ValueWrapper wrapper = this.get(key);
        if (wrapper != null) {
            return (T) wrapper.get();
        }
        try {
            T value = valueLoader.call();
            this.put(key, value);
            return value;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load value for key: " + key, e);
        }
    }

    /// 写入缓存，同时写入 L2 Redis 和 L1 本地缓存
    ///
    /// 注意：写入操作不广播通知其他节点，其他节点在读取时会从 L2 加载最新值
    ///
    /// 敏感缓存且未启用加密时仅写 L1，禁止明文写 Redis
    @Override
    public void put(Object key, Object value) {
        if (value == null) {
            log.debug("缓存值为空，跳过写入: cacheName={}, key={}", this.name, key);
            return;
        }
        String localKey = this.toLocalKey(key);
        if (this.isL1Only()) {
            this.localCache.put(localKey, value);
            log.debug("写入 L1-only 敏感缓存: cacheName={}, key={}", this.name, key);
            return;
        }
        this.redisCache.put(key, value);
        this.localCache.put(localKey, value);
        log.debug("写入二级缓存: cacheName={}, key={}", this.name, key);
    }

    /// 删除缓存，同时删除 L2 Redis 和 L1 本地缓存，并广播通知其他节点删除 L1
    ///
    /// 删除流程：
    /// - 删除 L2 Redis
    /// - 删除本机 L1
    /// - 发布 Artemis 广播消息
    /// - 其他节点收到消息后删除各自 L1
    @Override
    public void evict(Object key) {
        String localKey = this.toLocalKey(key);
        // 仍尝试删 Redis，清理可能存在的历史数据
        this.redisCache.evict(key);
        this.localCache.invalidate(localKey);
        log.debug("删除二级缓存: cacheName={}, key={}", this.name, key);
        this.publisher.publishEvict(this.name, localKey);
    }

    /// 清空缓存，同时清空 L2 Redis 和 L1 本地缓存，并广播通知其他节点清空 L1
    @Override
    public void clear() {
        this.redisCache.clear();
        this.localCache.invalidateAll();
        log.debug("清空二级缓存: cacheName={}", this.name);
        this.publisher.publishClear(this.name);
    }

    /// 将原始 key 转换为本地缓存使用的标准化字符串 key
    ///
    /// 为什么必须统一使用字符串 key：
    /// - 本地缓存原始 key 可能是任意对象类型（Long、String、自定义对象等）
    /// - Artemis 广播消息中的 key 只能是字符串
    /// - 如果本地缓存使用原始对象作为 key，广播消息使用字符串，会导致跨节点删除失败
    /// - 例如：本机 key=Long(1)，广播 key="1"，远端无法匹配
    ///
    /// 因此本地缓存必须统一使用字符串 key，与广播消息保持一致。
    ///
    /// @param key 原始缓存 key
    /// @return 标准化后的字符串 key
    private String toLocalKey(Object key) {
        if (key == null) {
            return "null";
        }
        return String.valueOf(key);
    }
}
