package cn.daxpay.open.channel.fuyou.config;

import cn.daxpay.open.channel.fuyou.client.FuyouChannelClient;
import cn.daxpay.open.channel.fuyou.client.FuyouDirectChannelClient;
import cn.daxpay.open.channel.fuyou.client.FuyouH5ChannelClient;
import cn.daxpay.open.channel.fuyou.client.FuyouInternetChannelClient;
import cn.daxpay.open.platform.common.config.properties.DaxpayChannelProperties;
import cn.daxpay.open.platform.common.spring.channel.ChannelRestClientSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/// # 富友通道客户端配置
///
/// 复用全局 RestClient，经 [ChannelRestClientSupport] 绑定 channel-two baseUrl 并强制挂载传输加密。
/// 服务商/直连/互联网直连/H5 收银台四客户端共用同一 baseUrl,
/// 端点路径前缀区分(`/channel/fuyou`、`/channel/fuyou/direct`、`/channel/fuyou/internet`、`/channel/fuyou/h5`)。
@Configuration
public class FuyouClientConfig {

    @Bean
    public FuyouChannelClient fuyouChannelClient(
            RestClient restClient,
            DaxpayChannelProperties channelProperties,
            ChannelRestClientSupport channelRestClientSupport) {
        return channelRestClientSupport.createClient(
                restClient, channelProperties.getTwo(), FuyouChannelClient.class);
    }

    @Bean
    public FuyouDirectChannelClient fuyouDirectChannelClient(
            RestClient restClient,
            DaxpayChannelProperties channelProperties,
            ChannelRestClientSupport channelRestClientSupport) {
        return channelRestClientSupport.createClient(
                restClient, channelProperties.getTwo(), FuyouDirectChannelClient.class);
    }

    @Bean
    public FuyouInternetChannelClient fuyouInternetChannelClient(
            RestClient restClient,
            DaxpayChannelProperties channelProperties,
            ChannelRestClientSupport channelRestClientSupport) {
        return channelRestClientSupport.createClient(
                restClient, channelProperties.getTwo(), FuyouInternetChannelClient.class);
    }

    @Bean
    public FuyouH5ChannelClient fuyouH5ChannelClient(
            RestClient restClient,
            DaxpayChannelProperties channelProperties,
            ChannelRestClientSupport channelRestClientSupport) {
        return channelRestClientSupport.createClient(
                restClient, channelProperties.getTwo(), FuyouH5ChannelClient.class);
    }
}
