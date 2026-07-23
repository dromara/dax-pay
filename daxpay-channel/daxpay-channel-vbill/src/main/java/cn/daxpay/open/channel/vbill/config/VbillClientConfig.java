package cn.daxpay.open.channel.vbill.config;

import cn.daxpay.open.channel.vbill.client.VbillChannelClient;
import cn.daxpay.open.platform.common.config.properties.DaxpayChannelProperties;
import cn.daxpay.open.platform.common.spring.channel.ChannelRestClientSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/// # 随行付通道客户端配置
///
/// 复用全局 RestClient，经 [ChannelRestClientSupport] 绑定 channel-two baseUrl 并强制挂载传输加密。
@Configuration
public class VbillClientConfig {

    @Bean
    public VbillChannelClient vbillChannelClient(
            RestClient restClient,
            DaxpayChannelProperties channelProperties,
            ChannelRestClientSupport channelRestClientSupport) {
        return channelRestClientSupport.createClient(
                restClient, channelProperties.getTwo(), VbillChannelClient.class);
    }
}
