package cn.daxpay.open.channel.umfpay.result.direct;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 联动优势直连密钥配置返回结果
@Data
@Accessors(chain = true)
public class UmfpayDirectKeyConfigResult {

    /// 通道商户号
    private String channelMchNo;

    /// 是否沙箱环境
    private Boolean sandbox;

    /// 联动优势商户号(mer_id)
    private String merId;

    /// 商户私钥(脱敏返回)
    @SensitiveInfo(front = 6, end = 6, compact = true)
    private String merchantPrivateKey;

    /// 联动优势平台公钥(脱敏返回)
    @SensitiveInfo(front = 6, end = 6, compact = true)
    private String platformPublicKey;

    /// 商户私钥是否已配置
    private boolean merchantPrivateKeyConfigured;

    /// 平台公钥是否已配置
    private boolean platformPublicKeyConfigured;
}
