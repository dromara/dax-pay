package cn.daxpay.open.channel.yizhifubj;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

/// # 一直付(北京)支付实现
///
/// 一直付(首信易支付, 5upay 网关)聚合支付通道, 支持微信/支付宝/云闪付扫码、
/// 支付宝/微信公众号 JsApi 与支付宝 WAP 跳转支付。主应用通过声明式 HTTP 客户端调用子应用
/// dax-pay-channel-two 完成实际通道交互(AES-128-ECB + RSA 数字信封 + MD5withRSA 签名)。
@AutoConfiguration
@ConfigurationPropertiesScan
@MapperScan(annotationClass = Mapper.class)
@ComponentScan
public class YizhifubjChannelApp {
}
