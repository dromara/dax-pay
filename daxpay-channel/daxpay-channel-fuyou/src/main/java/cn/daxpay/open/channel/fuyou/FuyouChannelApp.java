package cn.daxpay.open.channel.fuyou;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

/// # 富友支付实现
///
/// 主应用侧通道模块, 通过声明式 HTTP 客户端调用子应用 dax-pay-channel-two 的富友通道接口,
/// 并管理服务商密钥配置、支付策略注册、回调处理。
@AutoConfiguration
@ConfigurationPropertiesScan
@MapperScan(annotationClass = Mapper.class)
@ComponentScan
public class FuyouChannelApp {
}
