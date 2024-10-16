package org.dromara.daxpay;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * PC前端转发配置
 * @author xxm
 * @since 2024/10/6
 */
@Configuration
@RequiredArgsConstructor
public class FrontWebConfig implements WebMvcConfigurer {
    private final FrontH5Interceptor h5Interceptor;
    private final FrontWebInterceptor webInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(h5Interceptor).addPathPatterns("/h5/**");
        registry.addInterceptor(webInterceptor).addPathPatterns("/web/**");
    }
}
