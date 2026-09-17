package cn.daxpay.open.channel.sheng.result;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 盛付通通道密钥配置返回结果
@Data
@Accessors(chain = true)
public class ShengKeyConfigResult {

    /// 通道商户号
    private String channelMchNo;

    /// 盛付通商户号(mchId)
    private String shengMchId;

    /// 盛付通分配的 AppId
    private String sdpAppId;

    /// 商户RSA私钥(脱敏返回)
    @SensitiveInfo(front = 24, end = 24)
    private String merchantPrivateKey;

    /// 盛付通验签公钥(脱敏返回)
    @SensitiveInfo(front = 24, end = 24)
    private String shengpayPublicKey;

    /// 商户私钥是否已配置
    private boolean merchantPrivateKeyConfigured;

    /// 盛付通公钥是否已配置
    private boolean shengpayPublicKeyConfigured;
}
