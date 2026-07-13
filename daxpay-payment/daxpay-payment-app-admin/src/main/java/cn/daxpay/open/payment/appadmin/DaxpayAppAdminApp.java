package cn.daxpay.open.payment.appadmin;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/// # 运营移动端（APP/小程序）装配入口
///
/// 扫描 `cn.daxpay.open.payment.appadmin` 及其子包（controller / service）。
/// 本模块依赖 `daxpay-payment-admin`，通过 Service 转发复用运营端业务逻辑。
@AutoConfiguration
@ComponentScan
public class DaxpayAppAdminApp {
}
