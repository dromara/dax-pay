package cn.daxpay.open.payment.unipay;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/// # 开放支付端装配入口
///
/// 扫描 unipay 模块（trade / gateway / client），注册开放 API 与公开 H5 控制器。
/// 支付编排在 daxpay-payment-core（trade.runtime.service.pay.*）；本模块不反向被内核依赖。
@AutoConfiguration
@ComponentScan
public class DaxpayUnipayApp {
}
