package cn.daxpay.open.channel.yeepay.result.direct;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 易宝直连密钥配置返回结果
@Data
@Accessors(chain = true)
public class YeepayDirectKeyConfigResult {

    /// 通道商户号
    private String channelMchNo;

    /// 是否沙箱环境
    private Boolean sandbox;

    /// 易宝商户号(merchantNo)
    private String merchantNo;

    /// 易宝服务商商编(yopIsvNo)
    private String yopIsvNo;

    /// 通道应用 AppKey(脱敏返回)
    @SensitiveInfo(front = 6, end = 6)
    private String appKey;

    /// 商户 RSA 私钥(脱敏返回)
    @SensitiveInfo(front = 24, end = 24)
    private String privateKey;

    /// 易宝平台 RSA 公钥(脱敏返回)
    @SensitiveInfo(front = 24, end = 24)
    private String yopPublicKey;

    /// 微信 AppId
    private String wxAppId;

    /// 微信 AppSecret(脱敏返回)
    @SensitiveInfo(front = 6, end = 6)
    private String wxAppSecret;

    /// AppKey 是否已配置
    private boolean appKeyConfigured;

    /// 私钥是否已配置
    private boolean privateKeyConfigured;

    /// 公钥是否已配置
    private boolean yopPublicKeyConfigured;
}
