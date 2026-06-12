package org.dromara.daxpay.platform.common.mybatisplus.handler.encrypt;

import org.dromara.daxpay.platform.common.config.properties.PlatformConfigProperties;
import cn.hutool.core.collection.CollUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/// # 数据加密类型处理器初始化配置
///
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(PlatformConfigProperties.class)
public class DataEncryptTypeHandlerConfiguration {

    private final PlatformConfigProperties platformConfigProperties;

    @PostConstruct
    public void initEncryptTypeHandler() {
        var encrypt = platformConfigProperties.getEncrypt();

        if (!encrypt.isEnable()) {
            DataEncryptTypeHandler.initialize(null, false);
            return;
        }

        var keys = encrypt.getKeys();
        if (CollUtil.isEmpty(keys)) {
            throw new IllegalStateException("启用数据加密时必须配置至少一个密钥，请配置 daxpay.platform.config.encrypt.keys");
        }

        var encryptor = new SecureAesGcmEncryptor(keys);
        DataEncryptTypeHandler.initialize(encryptor, true);
    }
}
