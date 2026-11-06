package cn.daxpay.open.payment.app.admin.configuration;

import cn.daxpay.open.platform.capability.auth.interceptor.ClientCodeGuardInterceptor;
import cn.daxpay.open.platform.core.enums.client.ClientEnum;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/// # 运营移动端 Web 配置
///
/// `/app-admin/**` 为运营端(App)专属端点, 注册身份域守卫拦截 `x-client-code` 非 admin 的请求,
/// 与商户移动端 AppMerchantWebConfig 守卫对称。
@Configuration
public class AppAdminWebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 身份域守卫: 晚于 Sa-Token 鉴权(order 0), 未登录先报"用户未登录", 已登录但身份域不符再报客户端不匹配
        registry.addInterceptor(new ClientCodeGuardInterceptor(ClientEnum.ADMIN, "error.auth.clientCodeMismatchAdmin"))
                .addPathPatterns("/app-admin/**")
                .order(1);
    }
}
