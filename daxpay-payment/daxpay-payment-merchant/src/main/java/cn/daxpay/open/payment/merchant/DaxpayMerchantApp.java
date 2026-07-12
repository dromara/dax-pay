package cn.daxpay.open.payment.merchant;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/// # 商户自助端装配入口
///
/// 扫描 `cn.daxpay.open.payment.merchant` 及其子包，注册商户自助控制器（controller/mch）。
/// 本模块依赖 `daxpay-payment-common` 公共底座；商户领域 entity/dao/service 留公共层共享，
/// 此处仅负责商户端 HTTP 接口的装配。
@AutoConfiguration
@ComponentScan
public class DaxpayMerchantApp {
}
