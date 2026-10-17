package cn.daxpay.open.channel.baofu;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

/// # 宝付支付实现
///
/// 宝付聚合支付通道(官网 https://www.baofu.com/), 走聚合交易网关(juhe)统一下单线,
/// 支持微信/支付宝/云闪付的聚合收银台、扫码、JSAPI 与付款码支付。
/// 主应用通过声明式 HTTP 客户端调用子应用 dax-pay-channel-two 完成实际通道交互
/// (form 表单 + 商户 pfx 私钥 SHA256withRSA hex 签名 + 平台证书验签)。
@AutoConfiguration
@ConfigurationPropertiesScan
@MapperScan(annotationClass = Mapper.class)
@ComponentScan
public class BaofuChannelApp {
}
