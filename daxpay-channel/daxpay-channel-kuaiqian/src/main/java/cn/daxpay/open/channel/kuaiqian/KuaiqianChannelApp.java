package cn.daxpay.open.channel.kuaiqian;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

/// # 快钱支付实现
///
/// 快钱聚合支付通道(官网 https://www.99bill.com/), 走 MAS 统一网关当面付线(A7007 聚合扫码),
/// 支持微信/支付宝/云闪付主扫。主应用通过声明式 HTTP 客户端调用子应用 dax-pay-channel-two
/// 完成实际通道交互(PKCS7 数字信封 + SSL 客户端证书双向认证)。
@AutoConfiguration
@ConfigurationPropertiesScan
@MapperScan(annotationClass = Mapper.class)
@ComponentScan
public class KuaiqianChannelApp {
}
