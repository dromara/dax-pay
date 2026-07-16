package cn.daxpay.open.platform.capability.sensitiveword;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/// # 敏感词能力自动配置
///
@ComponentScan
@MapperScan(annotationClass = Mapper.class)
@AutoConfiguration
public class SensitiveWordAutoConfiguration {
}

