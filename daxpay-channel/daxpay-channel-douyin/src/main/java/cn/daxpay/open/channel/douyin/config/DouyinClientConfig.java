package cn.daxpay.open.channel.douyin.config;

import cn.daxpay.open.channel.douyin.client.DouyinChannelClient;
import cn.daxpay.open.platform.common.config.properties.DaxpayChannelProperties;
import cn.daxpay.open.platform.common.spring.channel.ChannelRestClientSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/// # 抖音通道客户端配置
///
/// 复用全局 RestClient，经 [ChannelRestClientSupport] 绑定 channel-one baseUrl 并强制挂载传输加密。
@Configuration
public class DouyinClientConfig {

    @Bean
    public DouyinChannelClient douyinChannelClient(
            RestClient restClient,
            DaxpayChannelProperties channelProperties,
            ChannelRestClientSupport channelRestClientSupport) {
        return channelRestClientSupport.createClient(
                restClient, channelProperties.getOne(), DouyinChannelClient.class);
    }
}
