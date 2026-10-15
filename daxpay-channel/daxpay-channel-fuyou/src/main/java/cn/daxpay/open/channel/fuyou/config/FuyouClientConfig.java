package cn.daxpay.open.channel.fuyou.config;

import cn.daxpay.open.channel.fuyou.client.FuyouChannelClient;
import cn.daxpay.open.channel.fuyou.client.FuyouDirectChannelClient;
import cn.daxpay.open.platform.common.config.properties.DaxpayChannelProperties;
import cn.daxpay.open.platform.common.spring.channel.ChannelRestClientSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/// # 富友通道客户端配置
///
/// 复用全局 RestClient，经 [ChannelRestClientSupport] 绑定 channel-two baseUrl 并强制挂载传输加密。
/// 服务商模式客户端与直连模式客户端共用同一 baseUrl, 端点路径前缀区分(`/channel/fuyou` 与 `/channel/fuyou/direct`)。
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
}
