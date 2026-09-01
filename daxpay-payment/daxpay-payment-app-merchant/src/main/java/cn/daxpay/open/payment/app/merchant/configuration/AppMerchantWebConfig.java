package cn.daxpay.open.payment.app.merchant.configuration;

import cn.daxpay.open.platform.core.enums.client.ClientEnum;
import cn.daxpay.open.platform.capability.auth.interceptor.ClientCodeGuardInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/// # 商户移动端 Web 配置
///
/// `/app-mch/**` 为商户端(App)专属端点, 注册身份域守卫拦截 `x-client-code` 非 merchant 的请求,
/// 避免其他端凭证(如运营端)落入业务层报"数据错误, 未发现商户号"这类误导性错误。
@Configuration
public class AppMerchantWebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 身份域守卫: 晚于 Sa-Token 鉴权(order 0), 未登录先报"用户未登录", 已登录但身份域不符再报客户端不匹配
        registry.addInterceptor(new ClientCodeGuardInterceptor(ClientEnum.MERCHANT))
                .addPathPatterns("/app-mch/**")
                .order(1);
    }
}
