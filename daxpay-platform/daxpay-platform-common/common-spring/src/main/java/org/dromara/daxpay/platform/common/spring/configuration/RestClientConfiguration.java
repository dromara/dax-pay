package org.dromara.daxpay.platform.common.spring.configuration;

import org.dromara.daxpay.platform.common.config.properties.PlatformCommonProperties;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

/// # RestClient 配置
///
@Configuration
@RequiredArgsConstructor
public class RestClientConfiguration {
    private final PlatformCommonProperties platformCommonProperties;

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

    // 4. 将 HttpClient 适配为 Spring 的 ClientHttpRequestFactory
    @Bean
    public HttpComponentsClientHttpRequestFactory httpRequestFactory(CloseableHttpClient httpClient) {
        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }

    // 5. 创建 RestClient Bean
    @Bean
    public RestClient restClient(HttpComponentsClientHttpRequestFactory httpRequestFactory) {
        return RestClient.builder()
                .requestFactory(httpRequestFactory)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
