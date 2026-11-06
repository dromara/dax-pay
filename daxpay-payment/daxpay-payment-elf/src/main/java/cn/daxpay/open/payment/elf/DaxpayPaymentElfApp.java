package cn.daxpay.open.payment.elf;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/// # PC收款精灵装配入口
///
/// 本模块为 PC 收款精灵专属端(`/elf/**`), 身份域仍为 merchant(`x-client-code` 头不变),
/// 依赖 `daxpay-payment-merchant`, 复用商户收银 Service; 精灵后续专属端点统一建在本模块。
@AutoConfiguration
@ComponentScan
public class DaxpayPaymentElfApp {
}
