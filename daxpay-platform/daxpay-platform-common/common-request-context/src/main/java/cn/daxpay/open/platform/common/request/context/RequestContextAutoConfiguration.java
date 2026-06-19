package cn.daxpay.open.platform.common.request.context;

import cn.daxpay.open.platform.common.request.context.filter.WebRequestContextFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/// # RequestContext 自动配置
///
@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class RequestContextAutoConfiguration {

    /// 注册 WebRequestContextFilter
    @Bean
    public FilterRegistrationBean<WebRequestContextFilter> webRequestContextFilter() {
        FilterRegistrationBean<WebRequestContextFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new WebRequestContextFilter());
        registration.setOrder(org.springframework.core.Ordered.HIGHEST_PRECEDENCE);
        registration.addUrlPatterns("/*");
        return registration;
    }
}
