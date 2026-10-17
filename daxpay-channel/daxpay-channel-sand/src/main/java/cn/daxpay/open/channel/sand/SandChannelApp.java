package cn.daxpay.open.channel.sand;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

/// # 杉德支付实现
///
/// 杉德支付通道(官网 https://www.sandpay.com.cn/), 走 v4 开放平台收款线(sd-receipts),
/// 直连商户模式(商户号 mid + 商户 pfx 私钥证书 + 杉德公钥证书), 协议为 JSON + RSA 数字信封。
/// 支持微信/支付宝/云闪付扫码与微信/支付宝 JSAPI(含小程序)。
/// 主应用通过声明式 HTTP 客户端调用子应用 dax-pay-channel-two 完成实际通道交互。
@AutoConfiguration
@ConfigurationPropertiesScan
@MapperScan(annotationClass = Mapper.class)
@ComponentScan
public class SandChannelApp {
}
