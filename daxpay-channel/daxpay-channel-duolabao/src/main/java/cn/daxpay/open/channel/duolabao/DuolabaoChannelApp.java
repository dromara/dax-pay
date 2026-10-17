package cn.daxpay.open.channel.duolabao;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

/// # 哆啦宝支付实现
///
/// 哆啦宝聚合支付通道(官网 https://www.duolabao.com/), 走开放平台 V4.0 接口
/// (聚合扫码 generateQRCodeUrl + JS 主动支付 createPayWithCheck), 支持微信/支付宝/云闪付。
/// 主应用通过声明式 HTTP 客户端调用子应用 dax-pay-channel-two 完成实际通道交互
/// (JSON 报文 + accessKey/timestamp/token Header 签名)。
@AutoConfiguration
@ConfigurationPropertiesScan
@MapperScan(annotationClass = Mapper.class)
@ComponentScan
public class DuolabaoChannelApp {
}
