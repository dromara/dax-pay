package cn.daxpay.open.channel.easypay.result;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 易支付通道密钥配置返回结果
@Data
@Accessors(chain = true)
public class EasyPayKeyConfigResult {

    /// 通道商户号
    private String channelMchNo;

    /// 易支付平台网关地址
    private String serverUrl;

    /// 易支付商户ID(pid)
    private String partnerId;

    /// 商户RSA私钥(脱敏返回)
    @SensitiveInfo(front = 24, end = 24)
    private String merchantPrivateKey;

    /// 易支付平台验签公钥(脱敏返回)
    @SensitiveInfo(front = 24, end = 24)
    private String platformPublicKey;

    /// 商户私钥是否已配置
    private boolean merchantPrivateKeyConfigured;

    /// 平台公钥是否已配置
    private boolean platformPublicKeyConfigured;
}
