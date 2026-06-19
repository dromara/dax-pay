package cn.daxpay.open.payment.merchant.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/// # 支付模块配置属性
///
@Data
@Component
@ConfigurationProperties(prefix = "daxpay")
public class DaxPayProperties {

}
