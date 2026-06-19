package cn.daxpay.open.platform.capability.file;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

/// # 文件存储自动配置
///
@ComponentScan
@ConfigurationPropertiesScan
@MapperScan(annotationClass = Mapper.class)
@AutoConfiguration
public class FileAutoConfiguration {

}
