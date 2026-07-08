package cn.daxpay.open.channel.dougong;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

/// # 斗拱(汇付天下)支付实现(主应用侧)
///
/// 主应用通过声明式 HTTP 客户端([DougongChannelClient])调用子应用 dax-pay-channel-two 完成实际通道交互。
/// 本模块不含汇付 SDK 依赖, 验签/签名/SDK 调用均由子应用承担(SDK 隔离)。
@AutoConfiguration
@ConfigurationPropertiesScan
@MapperScan(annotationClass = Mapper.class)
@ComponentScan
public class DougongChannelApp {
}
