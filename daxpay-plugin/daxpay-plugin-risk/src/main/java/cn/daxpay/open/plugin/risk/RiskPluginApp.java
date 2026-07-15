package cn.daxpay.open.plugin.risk;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/// # 支付风控插件自动配置
///
@AutoConfiguration
@MapperScan(annotationClass = Mapper.class)
@ComponentScan
public class RiskPluginApp {
}
