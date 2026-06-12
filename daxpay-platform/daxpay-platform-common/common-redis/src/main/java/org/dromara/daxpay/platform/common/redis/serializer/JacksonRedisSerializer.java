package org.dromara.daxpay.platform.common.redis.serializer;

import org.dromara.daxpay.platform.common.json.util.JacksonUtil;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/// # 基于平台标准 Jackson ObjectMapper 的 Redis JSON 序列化器
///
/// 序列化特点：
/// - 输出为可读 JSON 字符串，Redis 中可直接查看内容
/// - 不依赖多态类型信息，读取时由业务代码显式转换目标类型
/// - 统一使用平台标准 ObjectMapper，与接口 JSON、配置 JSON 行为一致
@Slf4j
public class JacksonRedisSerializer<T> implements RedisSerializer<T> {

    public static final JacksonRedisSerializer<Object> INSTANCE = new JacksonRedisSerializer<>();

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        try {
            ObjectMapper objectMapper = JacksonUtil.getObjectMapper();
            String json = objectMapper.writeValueAsString(t);
            return json.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        } catch (JacksonException e) {
            log.error("Redis JSON serialize failed: {}", e.getMessage(), e);
            throw new SerializationException("Could not serialize to JSON: " + e.getMessage(), e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            ObjectMapper objectMapper = JacksonUtil.getObjectMapper();
            String json = new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
            return (T) objectMapper.readValue(json, Object.class);
        } catch (Exception e) {
            log.error("Redis JSON deserialize failed: {}", e.getMessage(), e);
            throw new SerializationException("Could not deserialize from JSON: " + e.getMessage(), e);
        }
    }
}
