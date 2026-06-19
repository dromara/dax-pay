package cn.daxpay.open.platform.common.redis.configuration;

import cn.daxpay.open.platform.common.redis.serializer.JacksonRedisSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/// # Redis配置
///
/// 序列化策略：
/// - key：统一使用 StringRedisSerializer，Redis 中 key 为明文字符串
/// - value/hashValue：统一使用 JacksonRedisSerializer，输出可读 JSON，业务读取时显式转目标类型
///
/// Redis 使用约束：
/// - 禁止依赖多态自动恢复，必须显式指定目标类型
/// - 简单值（计数器、验证码、时间戳）优先使用 StringRedisTemplate
/// - 对象缓存使用 RedisTemplate<String, Object>，读取后显式转目标类型
@Configuration
@ConditionalOnClass(StringRedisTemplate.class)
public class RedisAutoConfiguration {

    /// 字符串类型Redis模板，适用于简单字符串值
    @Bean
    public StringRedisTemplate stringRedisTemplate(LettuceConnectionFactory connectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(connectionFactory);
        return template;
    }

    /// 默认Redis模板，value使用JSON序列化
    ///
    /// 注意：deserialize 返回 Object 类型，业务代码必须显式转换到目标类型。
    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory connectionFactory) {
        // key 序列化
        RedisSerializer<String> keySerializer = new StringRedisSerializer();
        // value 序列化：使用 Jackson JSON，可读且与平台 JSON 规范一致
        RedisSerializer<Object> valueSerializer = JacksonRedisSerializer.INSTANCE;
        // 配置 redisTemplate
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        // 普通 key 使用字符串序列化，保证 Redis 中 key 可直接阅读
        redisTemplate.setKeySerializer(keySerializer);
        // 普通 value 使用 JSON 序列化，统一对象缓存格式
        redisTemplate.setValueSerializer(valueSerializer);
        // hash 结构的 field 使用字符串序列化
        redisTemplate.setHashKeySerializer(keySerializer);
        // hash 结构的 value 使用 JSON 序列化，保持与普通 value 一致
        redisTemplate.setHashValueSerializer(valueSerializer);
        // 完成模板初始化，确保序列化器配置生效
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}


