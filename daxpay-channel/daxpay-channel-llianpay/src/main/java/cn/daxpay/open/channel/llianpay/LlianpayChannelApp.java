package cn.daxpay.open.channel.llianpay;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

/// # 连连支付实现
///
/// 连连聚合支付通道(官网 https://open.lianlianpay.com/), 走连连 openapi 统收单线
/// (统一下单/查单/退款 + 收银台), 支持微信/支付宝/云闪付扫码、微信 JSAPI/小程序、
/// 支付宝 JS、云闪付收银台跳转。主应用通过声明式 HTTP 客户端调用子应用 dax-pay-channel-two
/// 完成实际通道交互(JSON body + Signature-Data 头 MD5withRSA 签名)。
@AutoConfiguration
@ConfigurationPropertiesScan
@MapperScan(annotationClass = Mapper.class)
@ComponentScan
public class LlianpayChannelApp {
}
