package cn.bootx.platform.common.configuration;

import cn.bootx.platform.common.serializer.KryoRedisSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis配置
 *
 * @author xxm
 * @since 2020/4/9 15:40
 */
@Configuration
@ConditionalOnClass(StringRedisTemplate.class)
@RequiredArgsConstructor
public class RedisAutoConfiguration {

    @Bean
    @Primary
    public RedisTemplate<String, ?> redisTemplate(LettuceConnectionFactory connectionFactory) {
        // key序列化
        RedisSerializer<?> keySerializer = new StringRedisSerializer();
        // value序列化
        KryoRedisSerializer<Object> valueSerializer = new KryoRedisSerializer<>();
        // 配置redisTemplate
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        // key序列化
        redisTemplate.setKeySerializer(keySerializer);
        // value序列化
        redisTemplate.setValueSerializer(valueSerializer);
        // Hash key序列化
        redisTemplate.setHashKeySerializer(keySerializer);
        // Hash value序列化
        redisTemplate.setHashValueSerializer(valueSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
