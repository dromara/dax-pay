package cn.daxpay.open.channel.yisheng;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

/// # 易生支付实现
///
/// 易生聚合支付通道(官网 https://www.easypay.com.cn/), 走 api_gateway.do 统一网关(类支付宝表单+RSA 协议),
/// 支持微信/支付宝的扫码与 JSAPI 支付。主应用通过声明式 HTTP 客户端调用子应用 dax-pay-channel-two
/// 完成实际通道交互(直连商户模式: 合作伙伴ID+商户编号+RSA 密钥对)。
@AutoConfiguration
@ConfigurationPropertiesScan
@MapperScan(annotationClass = Mapper.class)
@ComponentScan
public class YishengChannelApp {
}
