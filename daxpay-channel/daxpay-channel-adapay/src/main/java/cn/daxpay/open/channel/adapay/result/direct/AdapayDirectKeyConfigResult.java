package cn.daxpay.open.channel.adapay.result.direct;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import lombok.Data;
import lombok.experimental.Accessors;

/// # Adapay 直连密钥配置返回结果
@Data
@Accessors(chain = true)
public class AdapayDirectKeyConfigResult {

    /// 通道商户号
    private String channelMchNo;

    /// Adapay 支付应用 ID
    private String adapayAppId;

    /// Adapay API Key(脱敏返回)
    @SensitiveInfo(front = 6, end = 6)
    private String apiKey;

    /// 商户 RSA 私钥(脱敏返回)
    @SensitiveInfo(front = 24, end = 24)
    private String privateKey;

    /// Adapay 平台公钥(脱敏返回)
    @SensitiveInfo(front = 24, end = 24)
    private String publicKey;

    /// API Key 是否已配置
    private boolean apiKeyConfigured;

    /// 私钥是否已配置
    private boolean privateKeyConfigured;
}
