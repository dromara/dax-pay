package org.dromara.daxpay.platform.capability.wechat;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/// # 微信模块自动配置
///
@Slf4j
@AutoConfiguration
@ComponentScan
@MapperScan(annotationClass = Mapper.class)
public class WechatAutoConfiguration {
}
