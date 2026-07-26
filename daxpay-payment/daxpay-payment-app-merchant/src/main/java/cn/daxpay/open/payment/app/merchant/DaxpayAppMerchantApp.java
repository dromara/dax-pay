package cn.daxpay.open.payment.app.merchant;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/// # 商户移动端（APP/小程序）装配入口
///
/// 本模块依赖 `daxpay-payment-merchant` 和 `daxpay-payment-admin`，通过 Service 转发复用商户端 / 运营端业务逻辑。
@AutoConfiguration
@ComponentScan
public class DaxpayAppMerchantApp {
}
