package cn.bootx.platform.common.cache.configuration;

import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import jakarta.annotation.Nullable;

import java.util.Objects;

/**
 * 自定义RedisCache, 缓存值为空不报错
 *
 * @author xxm
 * @since 2021/7/6
 */
public class BootxRedisCache extends RedisCache {

    /**
     * Create new {@link RedisCache}.
     * @param name must not be {@literal null}.
     * @param cacheWriter must not be {@literal null}.
     * @param cacheConfig must not be {@literal null}.
     */
    protected BootxRedisCache(String name, RedisCacheWriter cacheWriter, RedisCacheConfiguration cacheConfig) {
        super(name, cacheWriter, cacheConfig);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void put(Object key, @Nullable Object value) {
        // 允许为空或者非空值
        if (isAllowNullValues() || Objects.nonNull(value)) {
            super.put(key, value);
        }
    }

}
