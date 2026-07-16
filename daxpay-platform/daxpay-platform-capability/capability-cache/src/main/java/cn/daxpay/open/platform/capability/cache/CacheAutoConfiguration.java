package cn.daxpay.open.platform.capability.cache;

import cn.daxpay.open.platform.common.config.ConfigAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

/// # 缓存配置
///
/// 依赖 [ConfigAutoConfiguration] 先注册 SecureAesGcmEncryptor，供敏感缓存 L2 加密使用。
@ComponentScan
@ConfigurationPropertiesScan
@AutoConfiguration(after = ConfigAutoConfiguration.class)
public class CacheAutoConfiguration {

}
