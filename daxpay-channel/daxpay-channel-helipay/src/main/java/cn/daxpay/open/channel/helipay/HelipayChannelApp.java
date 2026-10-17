package cn.daxpay.open.channel.helipay;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

/// # 合利宝支付实现
///
/// 合利宝聚合支付通道(官网 https://www.helipay.com/, 一通道一产品 heli_pay,
/// 微信/支付宝扫码+公众号JS+H5 五项能力), 主应用侧策略与配置装配,
/// 协议实现在子应用 dax-pay-channel-two 的 daxpay-channel-helipay 模块
/// (扫码产品线 form+MD5 直连商户模式)。
@AutoConfiguration
@ConfigurationPropertiesScan
@MapperScan(annotationClass = Mapper.class)
@ComponentScan
public class HelipayChannelApp {
}
