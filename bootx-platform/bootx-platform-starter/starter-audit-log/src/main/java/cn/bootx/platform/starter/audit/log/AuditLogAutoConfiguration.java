package cn.bootx.platform.starter.audit.log;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * 审计模块
 *
 * @author xxm
 * @since 2021/11/8
 */
@ComponentScan
@ConfigurationPropertiesScan
@AutoConfiguration
@MapperScan(annotationClass = Mapper.class)
public class AuditLogAutoConfiguration {

}
