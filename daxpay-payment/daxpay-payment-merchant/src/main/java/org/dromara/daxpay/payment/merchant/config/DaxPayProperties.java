package org.dromara.daxpay.payment.merchant.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/// # 支付模块配置属性
///
@Data
@Component
@ConfigurationProperties(prefix = "daxpay")
public class DaxPayProperties {

    /// 当前部署所属的服务商号，商户端独立部署时配置
    private String isvNo;
}
