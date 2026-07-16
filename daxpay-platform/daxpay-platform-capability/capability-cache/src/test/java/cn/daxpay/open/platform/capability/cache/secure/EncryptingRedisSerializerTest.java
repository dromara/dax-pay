package cn.daxpay.open.platform.capability.cache.secure;

import cn.daxpay.open.platform.common.config.encrypt.SecureAesGcmEncryptor;
import cn.daxpay.open.platform.common.config.properties.EncryptKeyInfo;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.serializer.SerializationException;
import tools.jackson.databind.json.JsonMapper;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/// # 敏感缓存整包加密序列化器测试
class EncryptingRedisSerializerTest {

    private EncryptingRedisSerializer serializer;

    @BeforeAll
    static void initJackson() {
        if (JacksonUtil.getObjectMapper() == null) {
            JacksonUtil.setObjectMapper(JsonMapper.builder().build());
        }
    }

    @BeforeEach
    void setUp() {
        EncryptKeyInfo keyInfo = new EncryptKeyInfo();
        keyInfo.setVersion(1);
        keyInfo.setKey(generateKey(32));
        SecureAesGcmEncryptor encryptor = new SecureAesGcmEncryptor(List.of(keyInfo));
        serializer = new EncryptingRedisSerializer(encryptor);
    }

    @Test
    @DisplayName("整包加解密 round-trip 保留字段值")
    void shouldRoundTrip() {
        Map<String, Object> value = new LinkedHashMap<>();
        value.put("channelMchNo", "MCH001");
        value.put("apiKey", "sk_live_secret_key_value");
        value.put("privateKey", "-----BEGIN PRIVATE KEY-----\nABCxyz\n-----END PRIVATE KEY-----");

        byte[] bytes = serializer.serialize(value);
        Object restored = serializer.deserialize(bytes);

        assertInstanceOf(Map.class, restored);
        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) restored;
        assertEquals("MCH001", map.get("channelMchNo"));
        assertEquals("sk_live_secret_key_value", map.get("apiKey"));
        assertTrue(String.valueOf(map.get("privateKey")).contains("BEGIN PRIVATE KEY"));
    }

    @Test
    @DisplayName("序列化结果为 v1: 密文且不含明文密钥")
    void shouldProduceCipherWithoutPlainSecrets() {
        Map<String, Object> value = new LinkedHashMap<>();
        value.put("privateKey", "-----BEGIN PRIVATE KEY-----\nSUPER_SECRET\n-----END PRIVATE KEY-----");
        value.put("apiKey", "sk_live_plain_should_not_appear");

        byte[] bytes = serializer.serialize(value);
        String stored = new String(bytes, StandardCharsets.UTF_8);

        assertTrue(stored.startsWith("v1:"), "密文应带版本前缀 v1:");
        assertFalse(stored.contains("BEGIN PRIVATE KEY"), "Redis 存储不应含 PEM 明文");
        assertFalse(stored.contains("SUPER_SECRET"), "Redis 存储不应含私钥内容");
        assertFalse(stored.contains("sk_live_plain_should_not_appear"), "Redis 存储不应含 apiKey 明文");
    }

    @Test
    @DisplayName("null 序列化为空字节")
    void shouldSerializeNullAsEmpty() {
        assertEquals(0, serializer.serialize(null).length);
        assertNull(serializer.deserialize(null));
        assertNull(serializer.deserialize(new byte[0]));
    }

    @Test
    @DisplayName("非法密文反序列化抛 SerializationException")
    void shouldFailOnInvalidCipher() {
        assertThrows(SerializationException.class,
                () -> serializer.deserialize("not-a-cipher".getBytes(StandardCharsets.UTF_8)));
    }

    private static String generateKey(int length) {
        byte[] keyBytes = new byte[length];
        new SecureRandom().nextBytes(keyBytes);
        return Base64.getEncoder().encodeToString(keyBytes).substring(0, length);
    }
}
