package cn.daxpay.open.channel.stripe;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

/// # Stripe 通道实现
///
/// Stripe 通道(Visa/MasterCard 等)自动装配入口, 由 AutoConfiguration.imports 注册装配。
@AutoConfiguration
@ConfigurationPropertiesScan
@MapperScan(annotationClass = Mapper.class)
@ComponentScan
public class StripeChannelApp {
}
