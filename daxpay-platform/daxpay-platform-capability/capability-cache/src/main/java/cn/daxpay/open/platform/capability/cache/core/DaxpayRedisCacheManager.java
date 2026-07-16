package cn.daxpay.open.platform.capability.cache.core;

import cn.daxpay.open.platform.capability.cache.secure.SecureCacheNameMatcher;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;

/// # 自定义 Redis 缓存管理器
///
/// 作为二级缓存的 L2 层，负责跨节点数据共享
///
/// 按 cacheName 选择序列化策略：
/// - 敏感名（SecureCacheNameMatcher 命中）且已配置 secureConfig：整包 AES-GCM 加密
/// - 其它：明文 JSON
public class DaxpayRedisCacheManager extends RedisCacheManager {

    private final RedisCacheWriter cacheWriter;
    private final RedisCacheConfiguration defaultConfig;
    private final RedisCacheConfiguration secureConfig;
    private final SecureCacheNameMatcher secureMatcher;

    /// @param cacheWriter Redis 写入器
    /// @param defaultCacheConfiguration 普通缓存配置（明文 JSON）
    /// @param secureCacheConfiguration 敏感缓存配置（整包加密）；encrypt 未启用时可为 null
    /// @param secureMatcher 敏感缓存名匹配器
    public DaxpayRedisCacheManager(RedisCacheWriter cacheWriter,
                                   RedisCacheConfiguration defaultCacheConfiguration,
                                   RedisCacheConfiguration secureCacheConfiguration,
                                   SecureCacheNameMatcher secureMatcher) {
        super(cacheWriter, defaultCacheConfiguration);
        this.cacheWriter = cacheWriter;
        this.defaultConfig = defaultCacheConfiguration;
        this.secureConfig = secureCacheConfiguration;
        this.secureMatcher = secureMatcher;
    }

    /// 创建 Redis 缓存
    @Override
    @SuppressWarnings({ "ConstantConditions", "NullableProblems" })
    protected RedisCache createRedisCache(String name, RedisCacheConfiguration cacheConfig) {
        RedisCacheConfiguration config = this.resolveConfig(name, cacheConfig);
        return new DaxpayRedisCache(name, this.cacheWriter, config);
    }

    /// 按 cacheName 解析最终 RedisCacheConfiguration
    private RedisCacheConfiguration resolveConfig(String name, RedisCacheConfiguration cacheConfig) {
        // 敏感缓存且加密 L2 可用：强制使用 secure 序列化，避免 initialCacheConfigurations 漏配
        if (this.secureMatcher != null
                && this.secureMatcher.matches(name)
                && this.secureConfig != null) {
            return this.secureConfig;
        }
        if (cacheConfig != null) {
            return cacheConfig;
        }
        return this.defaultConfig;
    }
}
