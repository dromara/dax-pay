package cn.bootx.platform.common.spring.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.time.Duration;

/**
 * 跨域处理
 *
 * @author xxm
 * @since 2021/7/14
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@RequiredArgsConstructor
public class SpringCorsConfiguration {

    private final SpringProperties springProperties;

    @Bean
    @ConditionalOnProperty(prefix = "bootx-platform.common.spring.cors", value = "enable", havingValue = "true",
            matchIfMissing = true)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public FilterRegistrationBean<CorsFilter> corsWebFilter() {
        SpringProperties.Cors cors = springProperties.getCors();
        CorsConfiguration config = new CorsConfiguration();
        // 允许跨域发送身份凭证
        config.setAllowCredentials(true);
        // 预检请求有效期
        config.setMaxAge(Duration.ofMillis(cors.getMaxAge()));
        // 允许请求头
        config.addAllowedHeader(cors.getAllowedHeaders());
        // 允许请求方法
        config.addAllowedMethod(cors.getAllowedMethods());
        // 允许跨域的源为，注意与origin:*进行区分
        config.addAllowedOriginPattern(cors.getAllowedOriginPatterns());
        config.addExposedHeader(HttpHeaders.SET_COOKIE);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

}
