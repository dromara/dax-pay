package cn.daxpay.open.channel.adapay.result.direct;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 汇付天下直连密钥配置返回结果
@Data
@Accessors(chain = true)
public class AdapayDirectKeyConfigResult {

    /// 通道商户号
    private String channelMchNo;

    /// 汇付商户号
    private String merchantNo;

    /// 汇付支付应用 ID
    private String adapayAppId;

    /// 汇付 API Key(脱敏返回)
    @SensitiveInfo(front = 4, end = 4)
    private String apiKey;

    /// 商户 RSA 私钥(脱敏返回)
    @SensitiveInfo(front = 4, end = 4)
    private String privateKey;

    /// 汇付平台公钥(脱敏返回)
    @SensitiveInfo(front = 4, end = 4)
    private String publicKey;

    /// API Key 是否已配置
    private boolean apiKeyConfigured;

    /// 私钥是否已配置
    private boolean privateKeyConfigured;
}
