package cn.daxpay.open.channel.yeepay;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

/// # 易宝支付实现
///
/// 易宝聚合支付通道, 通过易宝 YOP 开放平台支持微信/支付宝/银联的扫码与 H5 支付。
/// 主应用通过声明式 HTTP 客户端调用子应用 dax-pay-channel-two 完成实际通道交互。
@AutoConfiguration
@ConfigurationPropertiesScan
@MapperScan(annotationClass = Mapper.class)
@ComponentScan
public class YeepayChannelApp {
}
