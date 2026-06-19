package cn.daxpay.open.channel.alipay;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

/// # 支付宝支付实现
///
/// 支付宝通道的自动配置入口，扫描当前模块下的所有组件(Controller/Service/Manager/Mapper等)，自动注册到Spring容器。
///
@AutoConfiguration
@ConfigurationPropertiesScan
@MapperScan(annotationClass = Mapper.class)
@ComponentScan
public class AlipayChannelApp {
}
