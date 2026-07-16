package cn.daxpay.open.platform.common.mybatisplus.handler.encrypt;

import cn.daxpay.open.platform.common.config.encrypt.SecureAesGcmEncryptor;
import cn.daxpay.open.platform.common.config.properties.PlatformConfigProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/// # 数据加密类型处理器初始化配置
///
/// 复用 [SecureAesGcmEncryptor] Spring Bean（由 [EncryptorConfiguration] 创建），
/// 保证与缓存 L2 加密使用同一加密器实例。
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(PlatformConfigProperties.class)
public class DataEncryptTypeHandlerConfiguration {

    private final PlatformConfigProperties platformConfigProperties;
    private final ObjectProvider<SecureAesGcmEncryptor> encryptorProvider;

    @PostConstruct
    public void initEncryptTypeHandler() {
        var encrypt = platformConfigProperties.getEncrypt();

        if (!encrypt.isEnable()) {
            DataEncryptTypeHandler.initialize(null, false);
            return;
        }

        SecureAesGcmEncryptor encryptor = encryptorProvider.getIfAvailable();
        if (encryptor == null) {
            throw new IllegalStateException(
                    "已启用数据加密但未找到 SecureAesGcmEncryptor Bean，请检查 daxpay.platform.config.encrypt 配置");
        }
        DataEncryptTypeHandler.initialize(encryptor, true);
    }
}
