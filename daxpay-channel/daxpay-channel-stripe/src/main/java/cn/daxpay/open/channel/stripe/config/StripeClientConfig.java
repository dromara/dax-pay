package cn.daxpay.open.channel.stripe.config;

import cn.daxpay.open.channel.stripe.client.StripeChannelClient;
import cn.daxpay.open.platform.common.config.properties.DaxpayChannelProperties;
import cn.daxpay.open.platform.common.spring.channel.ChannelRestClientSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/// # Stripe 通道客户端配置
///
/// 复用全局 RestClient，经 [ChannelRestClientSupport] 绑定 channel-three baseUrl 并强制挂载传输加密。
/// 国际通道统一走子应用三(dax-pay-channel-three, 端口 20300)。
@Configuration
public class StripeClientConfig {

    @Bean
    public StripeChannelClient stripeChannelClient(
            RestClient restClient,
            DaxpayChannelProperties channelProperties,
            ChannelRestClientSupport channelRestClientSupport) {
        return channelRestClientSupport.createClient(
                restClient, channelProperties.getThree(), StripeChannelClient.class);
    }
}
