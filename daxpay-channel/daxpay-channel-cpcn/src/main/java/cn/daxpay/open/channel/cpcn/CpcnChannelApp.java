package cn.daxpay.open.channel.cpcn;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

/// # 中金支付实现
///
/// 中金支付通道(官网 https://www.cpcn.com.cn/), 走 5011 统一下单线(被扫/跳转双大类),
/// 支持微信/支付宝/云闪付扫码、微信 JSAPI/H5、支付宝 JSAPI。主应用通过声明式 HTTP 客户端调用
/// 子应用 dax-pay-channel-two 完成实际通道交互(JSON 报文 + 国密 SM2 证书签名非数字信封模式)。
@AutoConfiguration
@ConfigurationPropertiesScan
@MapperScan(annotationClass = Mapper.class)
@ComponentScan
public class CpcnChannelApp {
}
