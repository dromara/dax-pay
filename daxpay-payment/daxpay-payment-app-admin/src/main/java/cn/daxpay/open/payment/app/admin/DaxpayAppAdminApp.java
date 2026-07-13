package cn.daxpay.open.payment.app.admin;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/// # 运营移动端（APP/小程序）装配入口
///
/// 本模块依赖 `daxpay-payment-admin`，通过 Service 转发复用运营端业务逻辑。
@AutoConfiguration
@ComponentScan
public class DaxpayAppAdminApp {
}
