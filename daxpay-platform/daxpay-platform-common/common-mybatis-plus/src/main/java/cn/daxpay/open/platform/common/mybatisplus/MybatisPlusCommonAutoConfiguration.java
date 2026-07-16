package cn.daxpay.open.platform.common.mybatisplus;

import cn.daxpay.open.platform.common.config.ConfigAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

/// # mybatis自动配置
///
/// 依赖 [ConfigAutoConfiguration] 先注册 SecureAesGcmEncryptor，供 DataEncryptTypeHandler 使用。
@AutoConfiguration(after = ConfigAutoConfiguration.class)
@ComponentScan
@ConfigurationPropertiesScan
public class MybatisPlusCommonAutoConfiguration {

}
