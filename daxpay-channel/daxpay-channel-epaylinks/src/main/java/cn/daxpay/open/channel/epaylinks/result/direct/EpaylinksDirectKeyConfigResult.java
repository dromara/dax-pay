package cn.daxpay.open.channel.epaylinks.result.direct;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 易票联直连密钥配置返回结果
@Data
@Accessors(chain = true)
public class EpaylinksDirectKeyConfigResult {

    /// 通道商户号
    private String channelMchNo;

    /// 是否沙箱环境
    private Boolean sandbox;

    /// 易票联商户编号(customerCode)
    private String customerCode;

    /// 上游商户号
    private String channelMchtNo;

    /// 证书序列号(脱敏返回)
    @SensitiveInfo(front = 2, end = 2)
    private String signNo;

    /// 商户私钥(脱敏返回)
    @SensitiveInfo(front = 6, end = 6, compact = true)
    private String merchantPrivateKey;

    /// 易票联平台公钥证书(脱敏返回)
    @SensitiveInfo(front = 6, end = 6, compact = true)
    private String platformCert;

    /// 证书序列号是否已配置
    private boolean signNoConfigured;

    /// 商户私钥是否已配置
    private boolean merchantPrivateKeyConfigured;

    /// 平台公钥证书是否已配置
    private boolean platformCertConfigured;
}
