package cn.daxpay.open.platform.common.i18n.config;

import cn.daxpay.open.platform.common.i18n.source.JsonMessageSource;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.util.Locale;

/// # 国际化自动配置
///
/// 注册 JsonMessageSource 作为 MessageSource 的实现,
/// Spring Boot 默认的 AcceptHeaderLocaleResolver 会自动生效,
/// 无需额外配置 LocaleResolver
@AutoConfiguration
public class I18nAutoConfiguration {

    @Bean
    public MessageSource messageSource(ResourcePatternResolver resourceResolver) {
        JsonMessageSource source = new JsonMessageSource(resourceResolver);
        source.setDefaultLocale(Locale.CHINA);
        // 初始化 I18nUtil
        I18nUtil.setMessageSource(source);
        return source;
    }
}
