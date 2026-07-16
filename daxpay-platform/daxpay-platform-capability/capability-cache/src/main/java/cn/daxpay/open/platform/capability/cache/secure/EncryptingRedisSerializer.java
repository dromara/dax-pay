package cn.daxpay.open.platform.capability.cache.secure;

import cn.daxpay.open.platform.common.config.encrypt.SecureAesGcmEncryptor;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.StandardCharsets;

/// # 敏感缓存 L2 整包加密序列化器
///
/// 将缓存对象序列化为 JSON 后，对**整段 JSON 字符串**做 AES-256-GCM 加密再写入 Redis。
/// 非字段级加密：密钥字段与非敏感字段一起进入同一密文包。
///
/// 流程：
/// - serialize: Object → JSON → encrypt → `v{n}:...` bytes
/// - deserialize: bytes → decrypt → JSON → Object
///
/// JSON 编解码与 JacksonRedisSerializer 一致，使用平台标准 ObjectMapper。
@Slf4j
public class EncryptingRedisSerializer implements RedisSerializer<Object> {

    private final SecureAesGcmEncryptor encryptor;

    public EncryptingRedisSerializer(SecureAesGcmEncryptor encryptor) {
        if (encryptor == null) {
            throw new IllegalArgumentException("SecureAesGcmEncryptor 不能为空");
        }
        this.encryptor = encryptor;
    }

    @Override
    public byte[] serialize(Object value) throws SerializationException {
        if (value == null) {
            return new byte[0];
        }
        try {
            ObjectMapper objectMapper = JacksonUtil.getObjectMapper();
            String json = objectMapper.writeValueAsString(value);
            String cipher = encryptor.encrypt(json);
            return cipher.getBytes(StandardCharsets.UTF_8);
        } catch (JacksonException e) {
            log.error("敏感缓存 JSON 序列化失败: {}", e.getMessage(), e);
            throw new SerializationException("Could not serialize to JSON for encrypt: " + e.getMessage(), e);
        } catch (RuntimeException e) {
            log.error("敏感缓存加密序列化失败: {}", e.getMessage(), e);
            throw new SerializationException("Could not encrypt-serialize cache value: " + e.getMessage(), e);
        }
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            String cipher = new String(bytes, StandardCharsets.UTF_8);
            String json = encryptor.decrypt(cipher);
            if (json == null) {
                throw new SerializationException("敏感缓存解密失败，密文格式或密钥版本无效");
            }
            ObjectMapper objectMapper = JacksonUtil.getObjectMapper();
            return objectMapper.readValue(json, Object.class);
        } catch (SerializationException e) {
            throw e;
        } catch (JacksonException e) {
            log.error("敏感缓存解密后 JSON 反序列化失败: {}", e.getMessage(), e);
            throw new SerializationException("Could not deserialize decrypted cache value: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("敏感缓存解密反序列化失败: {}", e.getMessage(), e);
            throw new SerializationException("Could not decrypt-deserialize cache value: " + e.getMessage(), e);
        }
    }
}
