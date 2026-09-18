package cn.daxpay.open.channel.easypay;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

/// # 易支付支付实现
///
/// 易支付三方聚合通道(一通道一产品 easy_pay, 一期扫码两类),
/// 主应用侧策略与配置装配, 协议实现在子应用 dax-pay-channel-two 的 daxpay-channel-easypay 模块。
@AutoConfiguration
@ConfigurationPropertiesScan
@MapperScan(annotationClass = Mapper.class)
@ComponentScan
public class EasyPayChannelApp {
}
