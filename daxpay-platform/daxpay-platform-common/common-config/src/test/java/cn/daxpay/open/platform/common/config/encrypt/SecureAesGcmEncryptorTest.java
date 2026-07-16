package cn.daxpay.open.platform.common.config.encrypt;

import cn.daxpay.open.platform.common.config.properties.EncryptKeyInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/// # AES-256-GCM 加密工具测试类
class SecureAesGcmEncryptorTest {

    private SecureAesGcmEncryptor encryptor;

    @BeforeEach
    void initEncryptor() {
        EncryptKeyInfo keyInfo = new EncryptKeyInfo();
        keyInfo.setVersion(1);
        keyInfo.setKey(generateKey(32));
        encryptor = new SecureAesGcmEncryptor(List.of(keyInfo));
    }

    @Test
    @DisplayName("生成指定长度的密钥")
    void shouldGenerateKey() {
        String key16 = generateKey(16);
        String key32 = generateKey(32);
        String key64 = generateKey(64);

        assertEquals(16, key16.length(), "16位密钥长度应正确");
        assertEquals(32, key32.length(), "32位密钥长度应正确");
        assertEquals(64, key64.length(), "64位密钥长度应正确");
    }

    @Test
    @DisplayName("加密和解密测试")
    void shouldEncryptAndDecrypt() {
        String plaintext = "这是一段需要加密的敏感数据，包含中文和特殊字符！@#$%";

        String encrypted = encryptor.encrypt(plaintext);
        String decrypted = encryptor.decrypt(encrypted);

        assertEquals(plaintext, decrypted, "解密后应与原文一致");
        assertNotEquals(plaintext, encrypted, "密文应与明文不同");
    }

    @Test
    @DisplayName("空字符串加密测试")
    void shouldEncryptAndDecryptEmptyString() {
        String plaintext = "";
        String encrypted = encryptor.encrypt(plaintext);
        String decrypted = encryptor.decrypt(encrypted);

        assertEquals(plaintext, decrypted, "空字符串解密后应与原文一致");
    }

    @Test
    @DisplayName("长文本加密测试")
    void shouldEncryptAndDecryptLongText() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            sb.append("这是一段很长的测试数据。");
        }
        String plaintext = sb.toString();

        String encrypted = encryptor.encrypt(plaintext);
        String decrypted = encryptor.decrypt(encrypted);

        assertEquals(plaintext, decrypted, "长文本解密后应与原文一致");
    }

    @Test
    @DisplayName("多密钥版本测试")
    void shouldEncryptAndDecryptWithMultipleKeyVersions() {
        EncryptKeyInfo keyV1 = new EncryptKeyInfo();
        keyV1.setVersion(1);
        keyV1.setKey(generateKey(32));

        EncryptKeyInfo keyV2 = new EncryptKeyInfo();
        keyV2.setVersion(2);
        keyV2.setKey(generateKey(32));

        SecureAesGcmEncryptor multiEncryptor = new SecureAesGcmEncryptor(List.of(keyV2, keyV1));

        String plaintext = "测试多密钥版本";
        String encrypted = multiEncryptor.encrypt(plaintext);
        String decrypted = multiEncryptor.decrypt(encrypted);

        assertEquals(2, multiEncryptor.getCurrentVersion(), "当前密钥版本应为v2");
        assertEquals(plaintext, decrypted, "多密钥版本解密后应与原文一致");
    }

    @Test
    @DisplayName("批量加密解密测试")
    void shouldBatchEncryptAndDecrypt() {
        String[] testData = {
            "用户名: admin",
            "密码: P@ssw0rd123!",
            "手机号: 13800138000",
            "身份证: 110101199001011234",
            "银行卡: 6222021234567890123"
        };

        for (String data : testData) {
            String encrypted = encryptor.encrypt(data);
            String decrypted = encryptor.decrypt(encrypted);
            assertEquals(data, decrypted, "批量加解密: [" + data + "] 解密后应与原文一致");
        }
    }

    /// 生成指定长度的随机密钥
    /// @param length 密钥长度
    /// @return Base64编码的密钥字符串
    private static String generateKey(int length) {
        byte[] keyBytes = new byte[length];
        new SecureRandom().nextBytes(keyBytes);
        return Base64.getEncoder().encodeToString(keyBytes).substring(0, length);
    }
}
