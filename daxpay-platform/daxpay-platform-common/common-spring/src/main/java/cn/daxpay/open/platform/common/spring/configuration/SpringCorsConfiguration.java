package cn.daxpay.open.platform.common.spring.configuration;

import cn.daxpay.open.platform.common.config.properties.PlatformCommonProperties;
import cn.hutool.core.text.CharSequenceUtil;
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
import java.util.Arrays;
import java.util.function.Consumer;

/// # 跨域处理
///
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@RequiredArgsConstructor
public class SpringCorsConfiguration {

    private final PlatformCommonProperties platformCommonProperties;

    @Bean
    @ConditionalOnProperty(prefix = "daxpay.platform.common.spring.cors", value = "enable", havingValue = "true")
    public FilterRegistrationBean<CorsFilter> corsWebFilter() {
        PlatformCommonProperties.Spring.Cors cors = platformCommonProperties.getSpring().getCors();
        CorsConfiguration config = new CorsConfiguration();
        // 允许跨域发送身份凭证
        config.setAllowCredentials(cors.isAllowCredentials());
        // 预检请求有效期
        config.setMaxAge(Duration.ofSeconds(cors.getMaxAge()));
        // 允许请求头
        addConfigValues(cors.getAllowedHeaders(), config::addAllowedHeader);
        // 允许请求方法
        addConfigValues(cors.getAllowedMethods(), config::addAllowedMethod);
        // 允许跨域的源为，注意与origin:*进行区分
        addConfigValues(cors.getAllowedOriginPatterns(), config::addAllowedOriginPattern);
        config.addExposedHeader(HttpHeaders.SET_COOKIE);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

    /// 添加 CORS 配置值
    ///
    /// @param value 配置值，逗号分隔
    /// @param consumer 添加配置的消费者
    private void addConfigValues(String value, Consumer<String> consumer) {
        if (CharSequenceUtil.isBlank(value)) {
            return;
        }
        Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(CharSequenceUtil::isNotBlank)
                .forEach(consumer);
    }

}

