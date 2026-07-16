package cn.daxpay.open.platform.common.config.encrypt;

import cn.daxpay.open.platform.common.config.properties.PlatformConfigProperties;
import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/// # 数据加密器 Spring 配置
///
/// 全进程唯一 [SecureAesGcmEncryptor] 实例，供 DB TypeHandler 与缓存 L2 加密序列化共用，
/// 避免两套密钥实例不一致。
///
/// 仅在 `daxpay.platform.config.encrypt.enable=true` 时注册 Bean。
@Slf4j
@Configuration
@EnableConfigurationProperties(PlatformConfigProperties.class)
public class EncryptorConfiguration {

    /// 创建 AES-GCM 加密器（启用加密时必须配置 keys）
    @Bean
    @ConditionalOnProperty(prefix = "daxpay.platform.config.encrypt", name = "enable", havingValue = "true")
    public SecureAesGcmEncryptor secureAesGcmEncryptor(PlatformConfigProperties platformConfigProperties) {
        var keys = platformConfigProperties.getEncrypt().getKeys();
        if (CollUtil.isEmpty(keys)) {
            throw new IllegalStateException(
                    "启用数据加密时必须配置至少一个密钥，请配置 daxpay.platform.config.encrypt.keys");
        }
        SecureAesGcmEncryptor encryptor = new SecureAesGcmEncryptor(keys);
        log.info("已注册 SecureAesGcmEncryptor Bean，当前密钥版本: v{}", encryptor.getCurrentVersion());
        return encryptor;
    }
}
