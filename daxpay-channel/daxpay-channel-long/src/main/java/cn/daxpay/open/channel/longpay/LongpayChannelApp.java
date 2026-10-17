package cn.daxpay.open.channel.longpay;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

/// # 建行龙支付实现
///
/// 建设银行龙支付聚合通道(建行 B2C 统一下单平台龙支付线, TXCODE=530590 聚合下单),
/// 一张聚合码承载微信/支付宝/银联/建行钱包主扫。直连商户模式(商户号+柜台号+分行号+双公钥,
/// 请求 MD5 摘要 + 回调 RSA 验签)。主应用通过声明式 HTTP 客户端调用子应用 dax-pay-channel-two
/// 完成实际通道交互。
///
/// 注意: 通道码为 long_pay, 但 Java 包名不能用保留字 long, 故使用 longpay。
/// 协议层依公开技术资料实现, 订单查询/退款接口待建行正式文档补齐(见各服务注释)。
@AutoConfiguration
@ConfigurationPropertiesScan
@MapperScan(annotationClass = Mapper.class)
@ComponentScan
public class LongpayChannelApp {
}
