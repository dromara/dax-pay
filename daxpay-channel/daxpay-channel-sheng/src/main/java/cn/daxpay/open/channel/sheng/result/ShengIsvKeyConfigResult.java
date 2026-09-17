package cn.daxpay.open.channel.sheng.result;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 盛付通服务商密钥配置返回结果
@Data
@Accessors(chain = true)
public class ShengIsvKeyConfigResult {

    /// 产品编码
    private String product;

    /// 服务商盛付通商户号(mchId)
    private String shengMchId;

    /// 服务商RSA私钥(脱敏返回)
    @SensitiveInfo(front = 24, end = 24)
    private String merchantPrivateKey;

    /// 盛付通验签公钥(脱敏返回)
    @SensitiveInfo(front = 24, end = 24)
    private String shengpayPublicKey;

    /// 服务商私钥是否已配置
    private boolean merchantPrivateKeyConfigured;

    /// 盛付通公钥是否已配置
    private boolean shengpayPublicKeyConfigured;
}
