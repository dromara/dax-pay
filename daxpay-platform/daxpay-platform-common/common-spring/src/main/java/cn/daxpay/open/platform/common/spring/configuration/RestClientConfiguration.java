package cn.daxpay.open.platform.common.spring.configuration;

import cn.daxpay.open.platform.common.config.properties.PlatformCommonProperties;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.Timeout;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

/// # RestClient 配置
///
/// 使用 Spring Boot 自动配置的 [RestClient.Builder], 以便 OpenTelemetry 的
/// ClientRequestObservation 拦截器自动追加(W3C traceparent 头自动透传)。
/// 另通过 BusinessContextInterceptor 透传业务上下文(国际化语言、终端编码)。
@Configuration
@RequiredArgsConstructor
public class RestClientConfiguration {
    private final PlatformCommonProperties platformCommonProperties;

    /// 业务上下文 header 名(与 WebHeaderCode 保持一致, 此处为避免底层模块依赖循环而硬编码)
    private static final String HEADER_ACCEPT_LANGUAGE = "accept-language";
    private static final String HEADER_X_CLIENT_CODE = "x-client-code";

    @Bean
    public CloseableHttpClient httpClient() {
        var rest = platformCommonProperties.getSpring().getRest();
        // 1. 连接池配置
        var connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(rest.getMaxTotal());          // 总连接数
        connectionManager.setDefaultMaxPerRoute(rest.getMaxPerRoute()); // 每个目标主机最大连接数

        // 3. 连接配置（TCP 层）
        var connectionConfig = ConnectionConfig.custom()
                .setConnectTimeout(Timeout.ofSeconds(rest.getConnectTimeout()))     // 建立 TCP 连接超时
                .setSocketTimeout(Timeout.ofSeconds(rest.getSocketTimeout()))     // 读取数据超时（单次 read）
                .setTimeToLive(Timeout.ofMinutes(5))         // 连接最大存活时间
                .build();
        connectionManager.setDefaultConnectionConfig(connectionConfig);
        // 4. 请求配置（HTTP 层）
        var requestConfig = RequestConfig.custom()
                .setResponseTimeout(Timeout.ofSeconds(rest.getResponseTimeout()))   // 整个响应读取超时（推荐用于支付）
                .setConnectionRequestTimeout(Timeout.ofSeconds(rest.getConnectionRequestTimeout())) // 从连接池获取连接超时
                .build();

        return HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .evictExpiredConnections()                   // 自动清理过期连接
                .evictIdleConnections(Timeout.ofSeconds(30)) // 清理空闲超过 30 秒的连接
                .disableCookieManagement()                   // 支付通常无状态，关闭 Cookie
                .disableAuthCaching()                        // 禁用认证缓存
                .disableAutomaticRetries()                   // 禁用重试
                .disableRedirectHandling()                   // 禁用重定向
                .build();
    }

    // 将 HttpClient 适配为 Spring 的 ClientHttpRequestFactory
    @Bean
    public HttpComponentsClientHttpRequestFactory httpRequestFactory(CloseableHttpClient httpClient) {
        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }

    /// 创建 RestClient Bean
    ///
    /// 优先使用 Spring Boot 自动配置的 RestClient.Builder(携带 OTel 拦截器,
    /// 自动透传 W3C traceparent); 若 autoconfiguration 未注册则 fallback 到 [RestClient#builder]。
    @Bean
    public RestClient restClient(ObjectProvider<RestClient.Builder> builderProvider,
                                 HttpComponentsClientHttpRequestFactory httpRequestFactory) {
        RestClient.Builder builder = builderProvider.getIfAvailable(RestClient::builder);
        return builder
                .requestFactory(httpRequestFactory)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .requestInterceptor(new BusinessContextInterceptor())
                .build();
    }

    /// 业务上下文透传拦截器
    ///
    /// OTel 仅自动透传 W3C traceparent, 业务上下文(国际化语言、终端编码)需手动透传。
    /// 通过 Spring 原生 [RequestContextHolder] 获取当前请求, 避免模块循环依赖。
    static class BusinessContextInterceptor implements ClientHttpRequestInterceptor {
        @Override
        public ClientHttpResponse intercept(org.springframework.http.HttpRequest request,
                                            byte[] body,
                                            ClientHttpRequestExecution execution) throws IOException {
            HttpServletRequest currentRequest = getCurrentRequest();
            if (currentRequest != null) {
                // 透传国际化语言(子应用异常消息按语言返回)
                String language = currentRequest.getHeader(HEADER_ACCEPT_LANGUAGE);
                if (StrUtil.isNotBlank(language)) {
                    request.getHeaders().set(HttpHeaders.ACCEPT_LANGUAGE, language);
                }
                // 透传终端编码(运营端/H5/小程序/API)
                String clientCode = currentRequest.getHeader(HEADER_X_CLIENT_CODE);
                if (StrUtil.isNotBlank(clientCode)) {
                    request.getHeaders().set(HEADER_X_CLIENT_CODE, clientCode);
                }
            }
            return execution.execute(request, body);
        }

        /// 获取当前线程绑定的 HttpServletRequest(若存在)
        private static HttpServletRequest getCurrentRequest() {
            RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
            if (attrs instanceof ServletRequestAttributes servletAttrs) {
                return servletAttrs.getRequest();
            }
            return null;
        }
    }
}
