package cn.daxpay.open.platform.baseapi;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/// # 框架基础功能
///
@AutoConfiguration
@ComponentScan
@MapperScan(annotationClass = Mapper.class)
public class BaseApiApplication {

}
