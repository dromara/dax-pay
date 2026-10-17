package cn.daxpay.open.channel.umfpay;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

/// # 联动优势支付实现
///
/// 联动优势聚合支付通道(官网 https://www.umfintech.com/), 走老网关 payservice.do 表单线
/// (RSA(SHA1) 表单签名 + HTML META 响应), 支持微信/支付宝/云闪付扫码与微信公众号跳转。
/// 主应用通过声明式 HTTP 客户端调用子应用 dax-pay-channel-two 完成实际通道交互。
@AutoConfiguration
@ConfigurationPropertiesScan
@MapperScan(annotationClass = Mapper.class)
@ComponentScan
public class UmfpayChannelApp {
}
