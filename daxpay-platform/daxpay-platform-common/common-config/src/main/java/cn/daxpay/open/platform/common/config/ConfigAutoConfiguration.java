package cn.daxpay.open.platform.common.config;

import cn.daxpay.open.platform.common.config.encrypt.EncryptorConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Import;

/// # 配置自动配置
///
@Slf4j
@AutoConfiguration
@ConfigurationPropertiesScan
@Import(EncryptorConfiguration.class)
public class ConfigAutoConfiguration {

}
