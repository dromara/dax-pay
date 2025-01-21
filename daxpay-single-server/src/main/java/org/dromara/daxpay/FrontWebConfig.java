package org.dromara.daxpay;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
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

    /**
     * 内嵌服务重定向
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(h5Interceptor).addPathPatterns("/h5/**");
        registry.addInterceptor(webInterceptor).addPathPatterns("/web/**");
    }

    /**
     * 重写资源路径配置 (内嵌服务时使用)
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 移动端网页映射
        registry.addResourceHandler("/h5/**").addResourceLocations("classpath:/static/h5/");
        // 电脑端网页映射
        registry.addResourceHandler("/web/**").addResourceLocations("classpath:/static/web/");
        // 上传文件访问映射
        registry.addResourceHandler("/storage/**").addResourceLocations("file:/data/files/");
    }
}
