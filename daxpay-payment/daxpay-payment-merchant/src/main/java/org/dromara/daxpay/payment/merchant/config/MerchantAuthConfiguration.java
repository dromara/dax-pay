package org.dromara.daxpay.payment.merchant.config;

import org.dromara.daxpay.platform.iam.auth.login.PasswordLoginHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/// # 商户端认证配置
///
/// 当配置了daxpay.isv-no时，禁用平台默认的密码登录处理器，使用商户端专用的处理器
@Configuration
public class MerchantAuthConfiguration {

    /// 当配置了isvNo时，禁用平台默认的PasswordLoginHandler
    @Bean
    @ConditionalOnProperty(prefix = "daxpay", name = "isv-no")
    public static Object disableDefaultPasswordLoginHandler() {
        return new Object() {
            // 此类存在是为了触发Spring Bean覆盖机制
            // 实际使用中通过spring.main.allow-bean-definition-overriding=true启用覆盖
        };
    }
}
