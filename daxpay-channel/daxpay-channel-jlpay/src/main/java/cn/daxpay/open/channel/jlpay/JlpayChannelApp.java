package cn.daxpay.open.channel.jlpay;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

/// # 嘉联支付实现
///
/// 嘉联聚合支付通道(官网 https://www.jlpay.com/), 走 OpenAPI 开放平台接口
/// (国密 SM2 签名验签), 一期支持微信/支付宝/云闪付的扫码主扫与付款码被扫。
/// 主应用通过声明式 HTTP 客户端调用子应用 dax-pay-channel-two 完成实际通道交互。
@AutoConfiguration
@ConfigurationPropertiesScan
@MapperScan(annotationClass = Mapper.class)
@ComponentScan
public class JlpayChannelApp {
}
