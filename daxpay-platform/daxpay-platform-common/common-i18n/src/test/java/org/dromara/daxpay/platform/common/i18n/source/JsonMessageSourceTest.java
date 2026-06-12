package org.dromara.daxpay.platform.common.i18n.source;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/// JsonMessageSource 加载与 key 解析测试
class JsonMessageSourceTest {

    private JsonMessageSource messageSource;

    @BeforeEach
    void setUp() {
        messageSource = new JsonMessageSource(new PathMatchingResourcePatternResolver());
        messageSource.setDefaultLocale(Locale.CHINA);
    }

    @Test
    void shouldResolvePayRouteNoMatchInZhCn() {
        String message = messageSource.getMessage("pay.route.error.noMatch", null, Locale.CHINA);
        assertEquals("未匹配到可用支付产品", message);
    }

    @Test
    void shouldResolvePayRouteNoMatchInEnUs() {
        String message = messageSource.getMessage("pay.route.error.noMatch", null, Locale.forLanguageTag("en-US"));
        assertEquals("No available payment product matched", message);
    }

    @Test
    void extractRelativePathShouldDecodeUrlEncodedJarPath() {
        String encoded = "jar:file:/app/common-i18n.jar!/i18n%2Fzh-CN%2Fpay%2Froute%2Ferror.json";
        String relative = messageSource.extractRelativePath(encoded, "zh-CN");
        assertEquals("pay.route.error", relative);
    }

    @Test
    void extractRelativePathShouldParsePlainClasspathUrl() {
        String url = "file:/project/common-i18n/target/classes/i18n/zh-CN/pay/route/error.json";
        String relative = messageSource.extractRelativePath(url, "zh-CN");
        assertEquals("pay.route.error", relative);
    }

    @Test
    void shouldLoadPayRouteErrorKeysFromClasspath() {
        String strategyNotFound = messageSource.getMessage("pay.route.error.strategyNotFound", null, Locale.CHINA);
        assertNotNull(strategyNotFound);
        assertEquals("应用未配置通道路由策略", strategyNotFound);
    }

    @Test
    void shouldResolveRouteModeNotExistWithPlaceholder() {
        String message = messageSource.getMessage("pay.route.error.routeModeNotExist", new Object[]{"invalid"}, Locale.CHINA);
        assertEquals("路由模式不存在: invalid", message);
    }

    @Test
    void shouldResolveChannelErrorInEnUs() {
        String message = messageSource.getMessage("channel.error.channelNotFound", null, Locale.forLanguageTag("en-US"));
        assertEquals("Unsupported channel code", message);
    }
}
