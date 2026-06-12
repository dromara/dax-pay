package org.dromara.daxpay.platform.common.spring;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

/// # spring默认配置
///
@EnableScheduling
@EnableRetry
@ComponentScan
@ConfigurationPropertiesScan
@AutoConfiguration
public class SpringAutoConfiguration {

}
