package cn.daxpay.open.channel.adapay;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

/// # Adapay 支付实现
///
/// Adapay 聚合支付通道, 通过 ADAPAY 网关支持微信/支付宝/银联的扫码/JSAPI/APP/H5/小程序/付款码支付。
/// 主应用通过声明式 HTTP 客户端调用子应用 dax-pay-channel-two 完成实际通道交互。
@AutoConfiguration
@ConfigurationPropertiesScan
@MapperScan(annotationClass = Mapper.class)
@ComponentScan
public class AdapayChannelApp {
}
