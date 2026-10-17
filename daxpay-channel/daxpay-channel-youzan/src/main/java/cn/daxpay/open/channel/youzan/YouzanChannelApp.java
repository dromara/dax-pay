package cn.daxpay.open.channel.youzan;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

/// # 有赞支付实现
///
/// 有赞支付(高汇通)聚合通道(官网 http://www.gaohuitong.com/), 走 youzan.pay.unified 统收单线
/// (微信 JSAPI/小程序/H5 + 支付宝 JSAPI)。主应用通过声明式 HTTP 客户端调用子应用 dax-pay-channel-two
/// 完成实际通道交互(JSON body + RSA(SHA256withRSA) 请求头签名, 直连商户模式)。
@AutoConfiguration
@ConfigurationPropertiesScan
@MapperScan(annotationClass = Mapper.class)
@ComponentScan
public class YouzanChannelApp {
}
