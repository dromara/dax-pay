package cn.daxpay.open.channel.douyin.config;

import cn.daxpay.open.channel.douyin.client.DouyinChannelClient;
import cn.daxpay.open.platform.common.config.properties.DaxpayChannelProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/// # 抖音通道客户端配置
///
/// 复用全局 [RestClient](由 common-spring 的 RestClientConfiguration 提供, 已预装:
/// OTel traceparent 透传 / BusinessContextInterceptor 业务上下文透传 / Apache HttpClient5 连接池),
/// 经 `mutate()` 派生出绑定 channel-one baseUrl 的实例。
///
/// baseUrl 从 [DaxpayChannelProperties] 统一读取。
@Configuration
public class DouyinClientConfig {

    @Bean
    public DouyinChannelClient douyinChannelClient(
            RestClient restClient,
            DaxpayChannelProperties channelProperties) {
        // mutate 派生: 继承全局 RestClient 的拦截器 / observation / requestFactory, 仅覆盖 baseUrl
        RestClient channelClient = restClient
                .mutate()
                .baseUrl(channelProperties.getOne().getBaseUrl())
                .build();
        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(channelClient))
                .build()
                .createClient(DouyinChannelClient.class);
    }
}
