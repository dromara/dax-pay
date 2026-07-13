package cn.daxpay.open.payment.merchant;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/// # 商户自助端装配入口
///
/// 扫描 `cn.daxpay.open.payment.merchant` 及其子包，注册：
/// - 商户端 HTTP 控制器（controller）
/// - 端特有能力：登录 Handler、商户上下文 Filter、本端查询 service（如 MerchantInfo 展示）
/// - 小程序编排 service
///
/// 本模块依赖 `daxpay-payment-core` 支付域内核；商户领域 entity/dao 与多端共享 service
/// （用户/应用/凭证/路由/权限等）仍在公共层，供 admin / unipay / 交易引擎共用。
@AutoConfiguration
@ComponentScan
public class DaxpayMerchantApp {
}
