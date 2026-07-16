package cn.daxpay.open.platform.common.config.encrypt;

import cn.daxpay.open.platform.common.config.properties.EncryptKeyInfo;
import cn.hutool.core.collection.CollUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

/// # 安全的 AES-256-GCM 加密工具（支持多密钥版本）
///
/// 适用于支付系统中加密各类敏感数据：DB 字段 TypeHandler、缓存 L2 整包 value 等。
///
/// 密文格式：v{version}:{base64(IV + AES-GCM-Encrypt(plaintext))}
/// 示例：v2:7Kf8jD2mNpQrStUvWxYz...
@Slf4j
public class SecureAesGcmEncryptor {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;
    private static final int KEY_LENGTH = 32;
    private static final String VERSION_PREFIX = "v";
    private static final String VERSION_SEPARATOR = ":";

    /// 当前密钥（用于加密）
    private final EncryptKeyInfo currentKey;

    /// 密钥映射表（版本号 -> 密钥信息），用于解密
    @Getter
    private final Map<Integer, EncryptKeyInfo> keyMap;

    /// 密钥对应的加密器缓存（版本号 -> SecretKey）
    private final Map<Integer, SecretKey> secretKeyCache;

    /// 构造函数
    /// @param keys 密钥列表，第一个为当前使用的密钥
    public SecureAesGcmEncryptor(List<EncryptKeyInfo> keys) {
        if (CollUtil.isEmpty(keys)) {
            throw new IllegalArgumentException("密钥列表不能为空");
        }

        // 校验密钥配置
        validateKeys(keys);

        // 第一个密钥为当前密钥
        this.currentKey = keys.getFirst();

        // 构建密钥映射表
        this.keyMap = keys.stream()
                .collect(Collectors.toMap(EncryptKeyInfo::getVersion, k -> k, (k1, k2) -> {
                    throw new IllegalArgumentException("存在重复的密钥版本号: " + k1.getVersion());
                }));

        // 初始化密钥缓存
        this.secretKeyCache = new HashMap<>();
        for (EncryptKeyInfo keyInfo : keys) {
            secretKeyCache.put(keyInfo.getVersion(), createSecretKey(keyInfo.getKey()));
        }

        // 记录日志
        List<Integer> historyVersions = keys.stream()
                .skip(1)
                .map(EncryptKeyInfo::getVersion)
                .toList();
        log.info("加密器初始化成功，当前版本: v{}，历史版本: {}",
                currentKey.getVersion(),
                historyVersions.isEmpty() ? "无" : historyVersions.stream().map(v -> "v" + v).toList());
    }

    /// 校验密钥配置
    private void validateKeys(List<EncryptKeyInfo> keys) {
        Set<Integer> versions = new HashSet<>();
        for (EncryptKeyInfo keyInfo : keys) {
            // 校验版本号
            if (keyInfo.getVersion() == null) {
                throw new IllegalArgumentException("密钥版本号不能为空");
            }
            if (versions.contains(keyInfo.getVersion())) {
                throw new IllegalArgumentException("存在重复的密钥版本号: " + keyInfo.getVersion());
            }
            versions.add(keyInfo.getVersion());

            // 校验密钥长度
            if (keyInfo.getKey() == null || keyInfo.getKey().length() != KEY_LENGTH) {
                throw new IllegalArgumentException("密钥版本 v" + keyInfo.getVersion() + " 的密钥长度需要为32位");
            }
        }
    }

    /// 创建密钥对象
    private SecretKey createSecretKey(String key) {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    /// 加密（使用当前密钥，添加版本前缀）
    /// @param plaintext 明文
    /// @return 密文，格式：v{version}:{encrypted}
    public String encrypt(String plaintext) {
        if (plaintext == null) {
            return null;
        }
        try {
            SecretKey secretKey = secretKeyCache.get(currentKey.getVersion());
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            byte[] iv = new byte[GCM_IV_LENGTH];
            new SecureRandom().nextBytes(iv);

            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);

            byte[] encrypted = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

            // 组合：IV + 密文
            byte[] combined = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

            String encryptedBase64 = Base64.getEncoder().encodeToString(combined);
            // 添加版本前缀
            return VERSION_PREFIX + currentKey.getVersion() + VERSION_SEPARATOR + encryptedBase64;
        } catch (Exception e) {
            throw new RuntimeException("加密失败", e);
        }
    }

    /// 解密（根据版本前缀选择密钥）
    /// @param ciphertext 密文，格式：v{version}:{encrypted}
    /// @return 明文，如果找不到对应密钥则返回null
    public String decrypt(String ciphertext) {
        if (ciphertext == null) {
            return null;
        }
        try {
            // 解析版本前缀
            if (!ciphertext.startsWith(VERSION_PREFIX)) {
                log.warn("密文格式错误，缺少版本前缀");
                return null;
            }

            int separatorIndex = ciphertext.indexOf(VERSION_SEPARATOR);
            if (separatorIndex == -1) {
                log.warn("密文格式错误，缺少版本分隔符");
                return null;
            }

            int version;
            try {
                version = Integer.parseInt(ciphertext.substring(1, separatorIndex));
            } catch (NumberFormatException e) {
                log.warn("密文版本号格式错误");
                return null;
            }

            String encryptedBase64 = ciphertext.substring(separatorIndex + 1);

            // 获取对应版本的密钥
            SecretKey secretKey = secretKeyCache.get(version);
            if (secretKey == null) {
                log.warn("找不到版本 v{} 对应的密钥", version);
                return null;
            }

            // 解密
            byte[] combined = Base64.getDecoder().decode(encryptedBase64);
            byte[] iv = new byte[GCM_IV_LENGTH];
            byte[] encrypted = new byte[combined.length - GCM_IV_LENGTH];

            System.arraycopy(combined, 0, iv, 0, iv.length);
            System.arraycopy(combined, iv.length, encrypted, 0, encrypted.length);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);

            byte[] decrypted = cipher.doFinal(encrypted);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("解密失败", e);
            return null;
        }
    }

    /// 获取当前密钥版本号
    public int getCurrentVersion() {
        return currentKey.getVersion();
    }

}
