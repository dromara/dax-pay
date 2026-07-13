package cn.daxpay.open.payment.unipay;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/// # 开放支付端装配入口
///
/// 扫描 unipay 模块（trade / gateway / client），注册开放 API 与公开 H5 控制器、编排服务。
/// 本模块依赖 daxpay-payment-core 支付域内核，不反向被内核依赖。
@AutoConfiguration
@ComponentScan
public class DaxpayUnipayApp {
}
