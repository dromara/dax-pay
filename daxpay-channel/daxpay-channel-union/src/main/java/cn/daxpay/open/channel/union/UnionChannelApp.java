package cn.daxpay.open.channel.union;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

/// # 云闪付(银联 ACP)支付实现
///
/// 云闪付对接银联全渠道支付平台, 通过证书签名支持主扫/被扫/H5 支付。
/// 主应用通过声明式 HTTP 客户端调用子应用 dax-pay-channel-one 完成实际通道交互。
@AutoConfiguration
@ConfigurationPropertiesScan
@MapperScan(annotationClass = Mapper.class)
@ComponentScan
public class UnionChannelApp {
}
