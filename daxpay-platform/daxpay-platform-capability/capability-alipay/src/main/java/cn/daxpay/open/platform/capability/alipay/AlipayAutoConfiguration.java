package cn.daxpay.open.platform.capability.alipay;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/// # 支付宝开放平台能力模块自动配置
///
/// 装配支付宝 OAuth 认证服务([AlipayAuthCapability], OpenAPI 直连无官方 SDK),
/// 供 iam 模块(授权登录)与 payment 模块(通道认证)共用, 不耦合配置存储。
///
@Slf4j
@AutoConfiguration
@ComponentScan
public class AlipayAutoConfiguration {
}
