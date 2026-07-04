package cn.daxpay.open.platform.common.i18n.source;

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

    /// 请求纯语言码 en（无 en/ 目录）时应反向匹配到 en-US 资源，而非回退默认中文
    @Test
    void shouldReverseMatchEnToEnUs() {
        String message = messageSource.getMessage("pay.route.error.noMatch", null, Locale.forLanguageTag("en"));
        assertEquals("No available payment product matched", message);
    }

    /// 请求纯语言码 zh（无 zh/ 目录）时应反向匹配到 zh-CN 资源
    @Test
    void shouldReverseMatchZhToZhCn() {
        String message = messageSource.getMessage("pay.route.error.noMatch", null, Locale.forLanguageTag("zh"));
        assertEquals("未匹配到可用支付产品", message);
    }

    /// 请求 zh-Hans（uni-app 中文 locale，无对应目录）时应反向匹配到 zh-CN 资源
    @Test
    void shouldReverseMatchZhHansToZhCn() {
        String message = messageSource.getMessage("pay.route.error.noMatch", null, Locale.forLanguageTag("zh-Hans"));
        assertEquals("未匹配到可用支付产品", message);
    }
}
