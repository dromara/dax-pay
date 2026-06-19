package cn.daxpay.open.platform.common.spring;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

/// # spring默认配置
///
@EnableScheduling
@ComponentScan
@ConfigurationPropertiesScan
@AutoConfiguration
public class SpringAutoConfiguration {

    /// 应用启动时设置 JVM 默认时区为 UTC
    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

}
