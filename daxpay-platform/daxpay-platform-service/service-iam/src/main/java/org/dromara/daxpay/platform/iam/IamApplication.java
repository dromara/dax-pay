package org.dromara.daxpay.platform.iam;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/// # 身份识别与访问管理
///
@AutoConfiguration
@ComponentScan
@MapperScan(annotationClass = Mapper.class)
public class IamApplication {

}
