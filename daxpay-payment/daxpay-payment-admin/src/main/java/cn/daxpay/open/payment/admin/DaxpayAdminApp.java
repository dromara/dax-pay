package cn.daxpay.open.payment.admin;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/// # 运营管理端装配入口
///
/// 扫描 `cn.daxpay.open.payment.admin` 及其子包（controller / service），注册管理端控制器与服务。
/// 本模块依赖 `daxpay-payment-core` 支付域内核，不反向被内核依赖。
@AutoConfiguration
@ComponentScan
public class DaxpayAdminApp {
}
