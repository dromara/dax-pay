package org.dromara.daxpay.payment.old.pay.channel;

import org.dromara.daxpay.payment.old.pay.channel.dto.ChannelPayReq;
import org.dromara.daxpay.payment.old.pay.channel.dto.ChannelPayResp;
import org.dromara.daxpay.payment.old.pay.channel.dto.ChannelResult;
import org.dromara.daxpay.platform.common.config.properties.PlatformCommonProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/// # 通道服务 HTTP 客户端
///
/// 封装对通道适配服务(daxpay-channel-one)的 HTTP 调用，统一处理地址拼接、结果解析与异常转换。
/// baseUrl 来自 [PlatformCommonProperties.ChannelOne] 配置。
///
@Slf4j
@Component
@RequiredArgsConstructor
public class ChannelPayClient {

    private final RestClient restClient;
    private final PlatformCommonProperties properties;

    /// 发起支付下单调用
    ///
    /// @param req 通道支付请求
    /// @return 通道支付响应
    public ChannelPayResp pay(ChannelPayReq req) {
        String url = properties.getChannelOne().getBaseUrl() + "/channel/pay";
        ChannelResult<ChannelPayResp> result = restClient.post()
                .uri(url)
                .body(req)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
        if (result == null || !result.isSuccess()) {
            // TODO 后续替换为国际化业务异常(如 BizInfoException + messageKey)
            String msg = result == null ? "通道服务无响应" : result.getMsg();
            throw new RuntimeException("通道支付调用失败: " + msg);
        }
        return result.getData();
    }
}
