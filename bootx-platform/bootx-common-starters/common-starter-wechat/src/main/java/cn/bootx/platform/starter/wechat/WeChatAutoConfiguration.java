package cn.bootx.platform.starter.wechat;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * 微信操作封装
 *
 * @author xxm
 * @since 2022/7/15
 */
@ComponentScan
@MapperScan(annotationClass = Mapper.class)
@AutoConfiguration
@ConfigurationPropertiesScan
public class WeChatAutoConfiguration {

}
