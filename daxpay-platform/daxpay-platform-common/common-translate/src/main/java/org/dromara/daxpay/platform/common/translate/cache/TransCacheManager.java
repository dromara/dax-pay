package org.dromara.daxpay.platform.common.translate.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/// # 翻译缓存管理器
///
/// 负责翻译结果的缓存、查询、写入，支持 LRU 淘汰策略 + TTL 过期
@Slf4j
@Component
public class TransCacheManager {

    /// 最大缓存条目数
    private static final int MAX_CACHE_SIZE = 10000;

    /// # 带过期时间的缓存条目
    ///
    @Getter
    @AllArgsConstructor
    private static class CacheEntry {
        private final Object value;
        private final long expireTime;

        boolean isExpired() {
            return System.currentTimeMillis() > expireTime;
        }
    }

    /// 翻译字段值缓存（LRU 淘汰 + TTL 过期）
    private final Map<TransCacheKey, CacheEntry> cache;

    public TransCacheManager() {
        this.cache = Collections.synchronizedMap(new LinkedHashMap<>() {
            @Override
            protected boolean removeEldestEntry(Map.Entry<TransCacheKey, CacheEntry> eldest) {
                return size() > MAX_CACHE_SIZE;
            }
        });
    }

    /// 检查缓存中是否存在指定键（已过期视为不存在）
    public boolean contains(TransCacheKey key) {
        CacheEntry entry = cache.get(key);
        if (entry == null) {
            return false;
        }
        if (entry.isExpired()) {
            cache.remove(key);
            return false;
        }
        return true;
    }

    /// 获取缓存值（已过期返回 null 并移除）
    public Object get(TransCacheKey key) {
        CacheEntry entry = cache.get(key);
        if (entry == null) {
            return null;
        }
        if (entry.isExpired()) {
            cache.remove(key);
            return null;
        }
        return entry.getValue();
    }

    /// 写入缓存（指定 TTL）
    /// @param ttlSeconds 存活时间（秒），0 表示不缓存
    public void put(TransCacheKey key, Object value, long ttlSeconds) {
        if (ttlSeconds <= 0) {
            return;
        }
        long expireTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(ttlSeconds);
        cache.put(key, new CacheEntry(value, expireTime));
    }
}


