package cn.daxpay.open.channel.youzan.result.direct;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 有赞直连密钥配置返回结果
@Data
@Accessors(chain = true)
public class YouzanDirectKeyConfigResult {

    /// 通道商户号
    private String channelMchNo;

    /// 是否沙箱环境
    private Boolean sandbox;

    /// 高汇通商户号(mch_id)
    private String merchantId;

    /// 商户 RSA 私钥(脱敏返回)
    @SensitiveInfo(front = 6, end = 6, compact = true)
    private String merchantPrivateKey;

    /// 有赞支付平台 RSA 公钥(脱敏返回)
    @SensitiveInfo(front = 6, end = 6, compact = true)
    private String platformPublicKey;

    /// 公钥唯一标识(publickeyid, 脱敏返回)
    @SensitiveInfo(front = 2, end = 2)
    private String publicKeyId;

    /// 商户私钥是否已配置
    private boolean merchantPrivateKeyConfigured;

    /// 平台公钥是否已配置
    private boolean platformPublicKeyConfigured;

    /// 公钥唯一标识是否已配置
    private boolean publicKeyIdConfigured;
}
