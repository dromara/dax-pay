package cn.daxpay.open.channel.ysep;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

/// # 银盛支付通道实现
///
/// 一通道两产品(ChannelEnum.YSEP_PAY):
/// - YSEP_PAY(银盛支付): 开放平台 method 族接口(qrcode/openapi 网关)
/// - YSEQT_PAY(银盛e企通): e企通 serviceNo 族接口(eqt.ysepay.com 网关)
@AutoConfiguration
@ConfigurationPropertiesScan
@MapperScan(annotationClass = Mapper.class)
@ComponentScan
public class YsepChannelApp {
}
