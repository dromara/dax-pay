package cn.daxpay.open.channel.mock.config;

import cn.daxpay.open.channel.mock.client.MockChannelClient;
import cn.daxpay.open.platform.common.config.properties.DaxpayChannelProperties;
import cn.daxpay.open.platform.common.spring.channel.ChannelRestClientSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/// # Mock 通道客户端配置
///
/// 通道执行链经通道子应用 one(与支付宝/微信等真实通道完全同构):
/// one 内 daxpay-channel-mock 模块扮演「模拟网关 SDK」角色, 以模拟网关协议(签名/验签/明文回调)
/// 调用模拟开放平台端点。
/// 复用全局 RestClient, 经 [ChannelRestClientSupport] 绑定 one 子应用 baseUrl 并强制挂载传输加密。
@Configuration
public class MockClientConfig {

    @Bean
    public MockChannelClient mockChannelClient(
            RestClient restClient,
            DaxpayChannelProperties channelProperties,
            ChannelRestClientSupport channelRestClientSupport) {
        return channelRestClientSupport.createClient(
                restClient, channelProperties.getOne(), MockChannelClient.class);
    }
}
