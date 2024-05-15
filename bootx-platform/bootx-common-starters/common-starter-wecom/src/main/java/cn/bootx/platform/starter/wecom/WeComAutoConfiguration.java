package cn.bootx.platform.starter.wecom;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * 企业微信
 *
 * @author xxm
 * @since 2022/7/22
 */
@ComponentScan
@ConfigurationPropertiesScan
@AutoConfiguration
@MapperScan(annotationClass = Mapper.class)
public class WeComAutoConfiguration {

}
