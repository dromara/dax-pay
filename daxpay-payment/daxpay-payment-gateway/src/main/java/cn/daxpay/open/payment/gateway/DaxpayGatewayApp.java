package cn.daxpay.open.payment.gateway;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/// # 支付网关端装配入口
///
/// 扫描 `cn.daxpay.open.payment.gateway` 及其子包，注册网关端控制器与编排服务。
/// 本模块依赖 `daxpay-payment-common` 公共底座，不反向被公共层依赖。
@AutoConfiguration
@ComponentScan
public class DaxpayGatewayApp {
}
