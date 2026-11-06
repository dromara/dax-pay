package cn.daxpay.open.payment.elf.configuration;

import cn.daxpay.open.platform.core.enums.client.ClientEnum;
import cn.daxpay.open.platform.capability.auth.interceptor.ClientCodeGuardInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/// # PC收款精灵 Web 配置
///
/// `/elf/**` 为 PC 收款精灵专属端点, 身份域仍为 merchant(精灵登录与请求头 `x-client-code` 均为 merchant),
/// 注册身份域守卫拦截非 merchant 的请求, 与商户端(Web)守卫对称, 避免其他端凭证落入收银业务层产生越权;
/// 商户上下文装载复用 [cn.daxpay.open.payment.merchant.filter.MchContextLocalFilter](按 `x-client-code` 请求头匹配, 与路径无关)。
@Configuration
public class ElfWebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 身份域守卫: 晚于 Sa-Token 鉴权(order 0), 未登录先报"用户未登录", 已登录但身份域不符再报客户端不匹配
        registry.addInterceptor(new ClientCodeGuardInterceptor(ClientEnum.MERCHANT))
                .addPathPatterns("/elf/**")
                .order(1);
    }
}
