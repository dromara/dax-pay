package cn.daxpay.open.channel.jeepay;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

/// # Jeepay计全支付实现
///
/// Jeepay 三方聚合通道(一通道一产品 jee_pay, 微信/支付宝/云闪付扫码+JSAPI 族),
/// 主应用侧策略与配置装配, 协议实现在子应用 dax-pay-channel-two 的 daxpay-channel-jeepay 模块。
@AutoConfiguration
@ConfigurationPropertiesScan
@MapperScan(annotationClass = Mapper.class)
@ComponentScan
public class JeepayChannelApp {
}
