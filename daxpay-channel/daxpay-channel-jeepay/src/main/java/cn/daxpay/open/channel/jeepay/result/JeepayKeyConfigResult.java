package cn.daxpay.open.channel.jeepay.result;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # Jeepay通道密钥配置返回结果
@Data
@Accessors(chain = true)
public class JeepayKeyConfigResult {

    /// 通道商户号
    private String channelMchNo;

    /// Jeepay平台网关地址
    private String serverUrl;

    /// Jeepay商户号(mchNo)
    private String jeepayMchNo;

    /// 应用ID(appId)
    private String appId;

    /// 应用密钥(脱敏返回)
    @SensitiveInfo(front = 8, end = 4, compact = true)
    private String appSecret;

    /// 应用密钥是否已配置
    private boolean appSecretConfigured;
}
