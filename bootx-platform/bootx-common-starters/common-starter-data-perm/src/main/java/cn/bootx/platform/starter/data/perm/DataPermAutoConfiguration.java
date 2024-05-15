package cn.bootx.platform.starter.data.perm;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * 数据权限
 *
 * @author xxm
 * @since 2021/11/23
 */
@ComponentScan
@ConfigurationPropertiesScan
@MapperScan(annotationClass = Mapper.class)
@AutoConfiguration
public class DataPermAutoConfiguration {

}
