package cn.daxpay.open.channel.dougong.config;

import cn.daxpay.open.channel.dougong.client.DougongChannelClient;
import cn.daxpay.open.platform.common.config.properties.DaxpayChannelProperties;
import cn.daxpay.open.platform.common.spring.channel.ChannelRestClientSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/// # 斗拱通道客户端配置
///
/// 复用全局 RestClient，经 [ChannelRestClientSupport] 绑定 channel-two baseUrl 并强制挂载传输加密。
@Configuration
public class DougongClientConfig {

    @Bean
    public DougongChannelClient dougongChannelClient(
            RestClient restClient,
            DaxpayChannelProperties channelProperties,
            ChannelRestClientSupport channelRestClientSupport) {
        return channelRestClientSupport.createClient(
                restClient, channelProperties.getTwo(), DougongChannelClient.class);
    }
}
