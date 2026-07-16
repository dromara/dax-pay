package cn.daxpay.open.platform.capability.cache.configuration;

import cn.daxpay.open.platform.capability.cache.core.MultiLevelCacheManager;
import cn.daxpay.open.platform.capability.cache.core.DaxpayRedisCacheManager;
import cn.daxpay.open.platform.capability.cache.core.LocalCacheRegistry;
import cn.daxpay.open.platform.capability.cache.notify.publisher.CacheInvalidationPublisher;
import cn.daxpay.open.platform.capability.cache.secure.EncryptingRedisSerializer;
import cn.daxpay.open.platform.capability.cache.secure.SecureCacheNameMatcher;
import cn.daxpay.open.platform.common.config.encrypt.SecureAesGcmEncryptor;
import cn.daxpay.open.platform.common.config.properties.PlatformCommonProperties;
import cn.daxpay.open.platform.common.redis.serializer.JacksonRedisSerializer;
import cn.daxpay.open.platform.common.artemis.service.ArtemisTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.stream.Collectors;

/// # 缓存自动配置
///
/// 支持 L1 本地缓存 + L2 Redis 缓存的二级缓存架构。
///
/// 设计要点：
/// - 缓存能力始终启用，不需要总开关控制
/// - L1 本地缓存始终启用，作为性能加速层
/// - L2 Redis 作为共享缓存主层
/// - 缓存失效通知通过 Artemis 广播，始终启用
/// - 名称匹配 secure: 的缓存：L2 整包 AES-GCM 加密；加密未启用时仅 L1
@Slf4j
@Configuration
@EnableCaching
@EnableConfigurationProperties(PlatformCommonProperties.class)
@ConditionalOnClass(CacheManager.class)
public class CachingConfiguration implements CachingConfigurer {

    private final PlatformCommonProperties platformCommonProperties;

    public CachingConfiguration(PlatformCommonProperties platformCommonProperties) {
        this.platformCommonProperties = platformCommonProperties;
    }

    /// 默认缓存 key 生成规则：类名:方法名:参数摘要
    ///
    /// 使用约束：
    /// - 包含类名和方法名，避免不同类同名方法冲突
    /// - 包含参数摘要，避免同方法不同参数冲突
    /// - cacheName 命名应遵循  模块:业务  风格
    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            String className = target.getClass().getSimpleName();
            String methodName = method.getName();
            String paramsSummary = params.length == 0 ? "" : java.util.Arrays.stream(params)
                    .map(p -> p == null ? "null" : p.toString())
                    .collect(Collectors.joining("_"));
            return className + ":" + methodName + (paramsSummary.isEmpty() ? "" : ":" + paramsSummary);
        };
    }

    /// 敏感缓存名匹配器
    @Bean
    @ConditionalOnMissingBean
    public SecureCacheNameMatcher secureCacheNameMatcher() {
        var cache = platformCommonProperties.getCache();
        return new SecureCacheNameMatcher(cache.getSecurePrefix(), cache.getSecureNames());
    }

    /// Redis 缓存管理器（L2）
    ///
    /// 作为二级缓存的 L2 层，负责跨节点数据共享
    ///
    /// 普通 cacheName 使用明文 JSON；敏感 cacheName 在 encryptor 可用时使用整包加密序列化。
    @Bean
    @ConditionalOnMissingBean(DaxpayRedisCacheManager.class)
    public DaxpayRedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory,
                                                     SecureCacheNameMatcher secureCacheNameMatcher,
                                                     ObjectProvider<SecureAesGcmEncryptor> encryptorProvider) {
        var l2Config = platformCommonProperties.getCache().getL2();
        Duration ttl = Duration.ofSeconds(l2Config.getDefaultTtl());
        RedisCacheConfiguration plainConfig = this.plainValueConfig(ttl);

        SecureAesGcmEncryptor encryptor = encryptorProvider.getIfAvailable();
        RedisCacheConfiguration secureConfig = null;
        if (encryptor != null) {
            EncryptingRedisSerializer encryptingSerializer = new EncryptingRedisSerializer(encryptor);
            secureConfig = this.secureValueConfig(ttl, encryptingSerializer);
            log.info("敏感缓存 L2 整包加密已启用，前缀: {}", secureCacheNameMatcher.getSecurePrefix());
        } else {
            log.warn("未启用数据加密：名称匹配 {} 的敏感缓存将仅使用 L1，不写 Redis",
                    secureCacheNameMatcher.getSecurePrefix());
        }

        return new DaxpayRedisCacheManager(
                RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory),
                plainConfig,
                secureConfig,
                secureCacheNameMatcher);
    }

    /// 本地缓存注册表
    ///
    /// 管理各个 cacheName 对应的本地缓存实例，L1 本地缓存始终启用
    @Bean
    public LocalCacheRegistry localCacheRegistry() {
        var l1Config = platformCommonProperties.getCache().getL1();
        return new LocalCacheRegistry(l1Config.getDefaultTtl(), l1Config.getMaximumSize());
    }

    /// 缓存失效通知发布者
    ///
    /// 通过 Artemis 广播缓存失效消息，通知其他节点删除本地 L1 缓存
    @Bean
    public CacheInvalidationPublisher cacheInvalidationPublisher(ArtemisTemplateService artemisTemplateService) {
        return new CacheInvalidationPublisher(artemisTemplateService);
    }

    /// 二级缓存管理器
    ///
    /// 作为 Spring 默认 CacheManager，通过 @Primary 标识。
    ///
    /// 为什么使用 @Primary：
    /// - 项目中存在多个 CacheManager（redisCacheManager 和 cacheManager）
    /// - 业务代码通过 @Cacheable 等注解使用缓存时，需要明确默认使用哪个 CacheManager
    /// - 二级缓存管理器是业务入口，应作为默认选择
    @Bean
    @Primary
    public CacheManager cacheManager(DaxpayRedisCacheManager redisCacheManager,
                                     LocalCacheRegistry localCacheRegistry,
                                     CacheInvalidationPublisher publisher,
                                     SecureCacheNameMatcher secureCacheNameMatcher,
                                     ObjectProvider<SecureAesGcmEncryptor> encryptorProvider) {
        boolean secureL2Enabled = encryptorProvider.getIfAvailable() != null;
        return new MultiLevelCacheManager(
                redisCacheManager,
                localCacheRegistry,
                publisher,
                secureCacheNameMatcher,
                secureL2Enabled
        );
    }

    /// 缓存管理器策略过期时间配置（普通缓存：明文 JSON value）
    private RedisCacheConfiguration plainValueConfig(Duration duration) {
        // redis缓存配置
        return RedisCacheConfiguration.defaultCacheConfig()
            // 设置key为String
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
            // 设置value 序列化方式为 JSON，与 RedisTemplate 保持一致
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(JacksonRedisSerializer.INSTANCE))
            // 不缓存null
            .disableCachingNullValues()
            // 覆盖默认的构造key，否则会多出一个冒号
            .computePrefixWith(name -> name + ":")
            // 过期时间
            .entryTtl(duration);
    }

    /// 缓存管理器策略过期时间配置（敏感缓存：整包 AES-GCM 加密 value）
    private RedisCacheConfiguration secureValueConfig(Duration duration, EncryptingRedisSerializer encryptingSerializer) {
        // redis缓存配置
        return RedisCacheConfiguration.defaultCacheConfig()
            // 设置key为String
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
            // 设置value 序列化方式为整包加密（JSON + AES-GCM）
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(encryptingSerializer))
            // 不缓存null
            .disableCachingNullValues()
            // 覆盖默认的构造key，否则会多出一个冒号
            .computePrefixWith(name -> name + ":")
            // 过期时间
            .entryTtl(duration);
    }

}
