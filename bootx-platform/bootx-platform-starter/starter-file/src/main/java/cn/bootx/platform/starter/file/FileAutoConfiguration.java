package cn.bootx.platform.starter.file;

import org.apache.ibatis.annotations.Mapper;
import org.dromara.x.file.storage.spring.EnableFileStorage;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * 文件管理
 *
 * @author xxm
 * @since 2022/1/12
 */
@ComponentScan
@ConfigurationPropertiesScan
@EnableFileStorage
@AutoConfiguration
@MapperScan(annotationClass = Mapper.class)
public class FileAutoConfiguration {

}
