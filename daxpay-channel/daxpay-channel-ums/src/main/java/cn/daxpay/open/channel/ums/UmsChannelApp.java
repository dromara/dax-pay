package cn.daxpay.open.channel.ums;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

/// # 银联商务支付实现
///
/// 银联商务聚合支付通道, 通过银联商务网关支持支付宝/微信/银联的扫码与 H5 支付。
/// 主应用通过声明式 HTTP 客户端调用子应用 dax-pay-channel-one 完成实际通道交互。
@AutoConfiguration
@ConfigurationPropertiesScan
@MapperScan(annotationClass = Mapper.class)
@ComponentScan
public class UmsChannelApp {
}
