package cn.daxpay.open.channel.alipay.config;

import cn.daxpay.open.channel.alipay.client.AlipayChannelClient;
import cn.daxpay.open.platform.common.config.properties.DaxpayChannelProperties;
import cn.daxpay.open.platform.common.spring.channel.ChannelRestClientSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/// # 支付宝通道客户端配置
///
/// 复用全局 RestClient，经 [ChannelRestClientSupport] 绑定 channel-one baseUrl 并强制挂载传输加密。
@Configuration
public class AlipayClientConfig {

    @Bean
    public AlipayChannelClient alipayChannelClient(
            RestClient restClient,
            DaxpayChannelProperties channelProperties,
            ChannelRestClientSupport channelRestClientSupport) {
        return channelRestClientSupport.createClient(
                restClient, channelProperties.getOne(), AlipayChannelClient.class);
    }
}
