package cn.bootx.platform.common.spring;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * spring默认配置
 * @author xxm
 * @since 2023/3/29
 */
@EnableScheduling
@EnableRetry
@ComponentScan
@ConfigurationPropertiesScan
public class SpringConfigApplication {

}
