package cn.daxpay.open.plugin.easypay;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/// # 易支付协议插件自动配置
///
@AutoConfiguration
@MapperScan(annotationClass = Mapper.class)
@ComponentScan
public class EasyPayPluginApp {
}
