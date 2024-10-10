package cn.bootx.platform.common.cache.manager;

import cn.bootx.platform.common.cache.configuration.BootxRedisCacheManager;
import cn.bootx.platform.common.cache.configuration.CachingProperties;
import cn.bootx.platform.common.serializer.KryoRedisSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * 缓存自动配置
 *
 * @author xxm
 * @since 2021/6/11
 */
@Configuration
@EnableCaching
@EnableConfigurationProperties(CachingProperties.class)
@ConditionalOnClass(CacheManager.class)
@ConditionalOnProperty(prefix = "bootx-platform.cache", value = "enabled", havingValue = "true", matchIfMissing = true)
public class CachingConfiguration implements CachingConfigurer {

    private final CachingProperties cachingProperties;

    private final KryoRedisSerializer<?> kryoRedisSerializer;

    public CachingConfiguration(CachingProperties cachingProperties, KryoRedisSerializer<?> kryoRedisSerializer) {
        this.cachingProperties = cachingProperties;
        this.kryoRedisSerializer = kryoRedisSerializer;
    }

    /**
     * 不配置key的情况,将方法名作为缓存key名称
     */
    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> method.getName();
    }

    /**
     * 缓存管理器
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {

        BootxRedisCacheManager bootxRedisCacheManager = new BootxRedisCacheManager(
                // Redis 缓存写入器
                RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory),
                // 默认配置
                this.getRedisCacheConfigurationWithTtl(Duration.ofSeconds(cachingProperties.getDefaultTtl())));
        bootxRedisCacheManager.setKeysTtl(cachingProperties.getKeysTtl());
        return bootxRedisCacheManager;

    }

    /**
     * 缓存管理器策略过期时间配置
     */
    private RedisCacheConfiguration getRedisCacheConfigurationWithTtl(Duration duration) {
        // redis缓存配置
        return RedisCacheConfiguration.defaultCacheConfig()
            // 设置key为String
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
            // 设置value 序列化方式
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(kryoRedisSerializer))
            // 不缓存null
            .disableCachingNullValues()
            // 覆盖默认的构造key，否则会多出一个冒号
            .computePrefixWith(name -> name + ":")
            // 过期时间
            .entryTtl(duration);
    }

}
