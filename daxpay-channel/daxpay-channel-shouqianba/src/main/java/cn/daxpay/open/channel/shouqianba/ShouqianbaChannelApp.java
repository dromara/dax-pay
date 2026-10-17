package cn.daxpay.open.channel.shouqianba;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

/// # 收钱吧支付实现
///
/// 收钱吧聚合支付通道(官网 https://www.shouqianba.com/), 走 upay v2 协议(终端号+终端密钥直连商户模式),
/// 支持支付宝/微信/云闪付主扫与微信 WAP 收银台。主应用通过声明式 HTTP 客户端调用子应用 dax-pay-channel-two
/// 完成实际通道交互(MD5 请求签名 + RSA-SHA256 回调验签)。
@AutoConfiguration
@ConfigurationPropertiesScan
@MapperScan(annotationClass = Mapper.class)
@ComponentScan
public class ShouqianbaChannelApp {
}
