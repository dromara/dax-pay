package cn.daxpay.open.payment.admin.configuration;

import cn.daxpay.open.platform.capability.auth.interceptor.ClientCodeGuardInterceptor;
import cn.daxpay.open.platform.core.enums.client.ClientEnum;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/// # 运营端(Web)Web 配置
///
/// `/admin/**` 为运营端(Web)专属端点, 注册身份域守卫拦截 `x-client-code` 非 admin 的请求。
///
/// ## 背景
/// Sa-Token 鉴权只校验登录态、不校验身份域, 未守卫时其他端(如商户端)的有效凭证同样可达
/// 工作台统计等登录即放行([IgnoreAuth] login=true)的端点, 造成跨身份域信息泄露
/// (商户端子账号可读平台全局交易趋势/商户计数); 与商户端 [MerchantWebConfig] 守卫对称。
@Configuration
public class AdminWebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 身份域守卫: 晚于 Sa-Token 鉴权(order 0), 未登录先报"用户未登录", 已登录但身份域不符再报客户端不匹配
        registry.addInterceptor(new ClientCodeGuardInterceptor(ClientEnum.ADMIN, "error.auth.clientCodeMismatchAdmin"))
                .addPathPatterns("/admin/**")
                .order(1);
    }
}
