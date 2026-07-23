package cn.daxpay.open.platform.common.config.encrypt;

import cn.daxpay.open.platform.core.exception.BizErrorException;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

/// # 通道传输 AES-256-GCM 加密器测试
class ChannelAesGcmEncryptorTest {

    private ChannelAesGcmEncryptor encryptor;

    @BeforeEach
    void initEncryptor() {
        encryptor = new ChannelAesGcmEncryptor(generateKey(32));
    }

    @Test
    @DisplayName("密钥长度非法应抛业务异常")
    void shouldRejectInvalidKeyLength() {
        assertThrows(BizInfoException.class, () -> new ChannelAesGcmEncryptor(null));
        assertThrows(BizInfoException.class, () -> new ChannelAesGcmEncryptor("short"));
        assertThrows(BizInfoException.class, () -> new ChannelAesGcmEncryptor(generateKey(31)));
    }

    @Test
    @DisplayName("加密和解密往返一致")
    void shouldEncryptAndDecrypt() {
        String plaintext = "这是一段需要加密的敏感数据，包含中文和特殊字符！@#$%";

        String encrypted = encryptor.encrypt(plaintext);
        String decrypted = encryptor.decrypt(encrypted);

        assertEquals(plaintext, decrypted);
        assertNotEquals(plaintext, encrypted);
        // 无版本前缀
        assertFalse(encrypted.startsWith("v"));
    }

    @Test
    @DisplayName("空字符串加密测试")
    void shouldEncryptAndDecryptEmptyString() {
        String encrypted = encryptor.encrypt("");
        assertEquals("", encryptor.decrypt(encrypted));
    }

    @Test
    @DisplayName("长文本加密测试")
    void shouldEncryptAndDecryptLongText() {
        String plaintext = "这是一段很长的测试数据。".repeat(100);
        assertEquals(plaintext, encryptor.decrypt(encryptor.encrypt(plaintext)));
    }

    @Test
    @DisplayName("同一明文两次密文不同（随机 IV）")
    void shouldProduceDifferentCiphertext() {
        String plaintext = "{\"amount\":100}";
        String a = encryptor.encrypt(plaintext);
        String b = encryptor.encrypt(plaintext);
        assertNotEquals(a, b);
        assertEquals(plaintext, encryptor.decrypt(a));
        assertEquals(plaintext, encryptor.decrypt(b));
    }

    @Test
    @DisplayName("错误密钥解密应失败")
    void shouldFailWithWrongKey() {
        String encrypted = encryptor.encrypt("{\"ok\":true}");
        ChannelAesGcmEncryptor other = new ChannelAesGcmEncryptor(generateKey(32));
        assertThrows(BizErrorException.class, () -> other.decrypt(encrypted));
    }

    /// 生成指定长度的随机密钥字符串
    private static String generateKey(int length) {
        byte[] keyBytes = new byte[length];
        new SecureRandom().nextBytes(keyBytes);
        return Base64.getEncoder().encodeToString(keyBytes).substring(0, length);
    }
}
