package cn.daxpay.open.channel.xingyi;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

/// # 星驿付支付实现
///
/// 星驿付(新大陆旗下国通星驿, 官网 https://www.postar.cn/), 驿云付 YYF 接口,
/// 聚合支持微信/支付宝/云闪付(官方码扫码/星驿码聚合/JSAPI/小程序/付款码)。
/// 主应用通过声明式 HTTP 客户端调用子应用 dax-pay-channel-two 完成实际通道交互
/// (JSON 报文 + SHA256 摘要 + 商户 RSA 公钥加密式签名)。
@AutoConfiguration
@ConfigurationPropertiesScan
@MapperScan(annotationClass = Mapper.class)
@ComponentScan
public class XingyiChannelApp {
}
