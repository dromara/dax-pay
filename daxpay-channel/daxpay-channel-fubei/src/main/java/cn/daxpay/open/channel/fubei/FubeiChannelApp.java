package cn.daxpay.open.channel.fubei;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

/// # 付呗支付实现
///
/// 付呗聚合支付通道(官网 https://www.51fubei.com/), 走开放平台统一网关(JSON + MD5):
/// 云闪付扫码 / 微信JSAPI / 支付宝JSAPI / 支付宝H5。主应用通过声明式 HTTP 客户端调用子应用
/// dax-pay-channel-two 完成实际通道交互。
@AutoConfiguration
@ConfigurationPropertiesScan
@MapperScan(annotationClass = Mapper.class)
@ComponentScan
public class FubeiChannelApp {
}
