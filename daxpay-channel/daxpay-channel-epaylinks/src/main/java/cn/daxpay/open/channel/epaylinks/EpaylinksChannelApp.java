package cn.daxpay.open.channel.epaylinks;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

/// # 易票联支付实现
///
/// 易票联聚合支付通道(官网 https://www.epaylinks.cn/), 走 EFPS 统一网关
/// (微信/支付宝/云闪付主扫 + 微信/支付宝 JSAPI + 个人网银收银台)。
/// 主应用通过声明式 HTTP 客户端调用子应用 dax-pay-channel-two
/// 完成实际通道交互(JSON body + SHA256withRSA 请求头签名)。
@AutoConfiguration
@ConfigurationPropertiesScan
@MapperScan(annotationClass = Mapper.class)
@ComponentScan
public class EpaylinksChannelApp {
}
