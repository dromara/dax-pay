package cn.daxpay.open.channel.hmpay.config;

import cn.daxpay.open.channel.hmpay.client.HmpayChannelClient;
import cn.daxpay.open.platform.common.config.properties.DaxpayChannelProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/// # 河马付通道客户端配置
///
/// 复用全局 [RestClient](由 common-spring 的 RestClientConfiguration 提供, 已预装:
/// OTel traceparent 透传 / BusinessContextInterceptor 业务上下文透传 / Apache HttpClient5 连接池),
/// 经 `mutate()` 派生出绑定 channel-two baseUrl 的实例。
///
/// baseUrl 从 [DaxpayChannelProperties] 统一读取(河马付部署在 channel-two 子应用)。
@Configuration
public class HmpayClientConfig {

    @Bean
    public HmpayChannelClient hmpayChannelClient(
            RestClient restClient,
            DaxpayChannelProperties channelProperties) {
        // mutate 派生: 继承全局 RestClient 的拦截器 / observation / requestFactory, 仅覆盖 baseUrl
        RestClient channelClient = restClient
                .mutate()
                .baseUrl(channelProperties.getTwo().getBaseUrl())
                .build();
        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(channelClient))
                .build()
                .createClient(HmpayChannelClient.class);
    }
}
