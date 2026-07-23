package cn.daxpay.open.platform.common.spring.channel;

import cn.daxpay.open.platform.common.config.encrypt.ChannelAesGcmEncryptor;
import cn.daxpay.open.platform.common.config.properties.DaxpayChannelProperties;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/// # 通道 RestClient 工厂
///
/// 为各 HttpExchange 通道 Client 派生带 baseUrl + 传输加密拦截器的 RestClient。
/// 传输加密强制常开：key 非法则创建 Client 时直接失败，避免带病启动后明文外泄。
@Slf4j
@Component
public class ChannelRestClientSupport {

    /// 通道子应用配置不能为空
    public static final String MSG_APP_CONFIG_MISSING = "channel.error.transportEncrypt.appConfigMissing";
    /// 缺少通道传输加密配置
    public static final String MSG_CONFIG_MISSING = "channel.error.transportEncrypt.configMissing";

    /// 创建声明式通道 HTTP Client
    /// @param restClient 全局 RestClient（已含 OTel / 业务上下文拦截器）
    /// @param app 子应用连接与传输加密配置
    /// @param clientType @HttpExchange 接口类型
    public <T> T createClient(RestClient restClient,
                              DaxpayChannelProperties.ChannelApp app,
                              Class<T> clientType) {
        if (app == null) {
            // 通道子应用配置不能为空
            throw new BizInfoException(CommonErrorCode.SYSTEM_ERROR, MSG_APP_CONFIG_MISSING, clientType.getSimpleName());
        }
        var transportEncrypt = app.getTransportEncrypt();
        if (transportEncrypt == null) {
            // 缺少通道传输加密配置
            throw new BizInfoException(CommonErrorCode.SYSTEM_ERROR, MSG_CONFIG_MISSING, clientType.getSimpleName());
        }
        ChannelAesGcmEncryptor encryptor = new ChannelAesGcmEncryptor(transportEncrypt.getKey());

        RestClient channelClient = restClient
                .mutate()
                .baseUrl(app.getBaseUrl())
                .requestInterceptor(new ChannelTransportEncryptInterceptor(encryptor))
                .build();

        log.info("已创建通道 Client {}，baseUrl={}，传输加密=强制开启",
                clientType.getSimpleName(), app.getBaseUrl());

        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(channelClient))
                .build()
                .createClient(clientType);
    }
}
