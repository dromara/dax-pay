package cn.daxpay.open.channel.sand.result.direct;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 杉德直连密钥配置返回结果
@Data
@Accessors(chain = true)
public class SandDirectKeyConfigResult {

    /// 通道商户号
    private String channelMchNo;

    /// 是否沙箱环境
    private Boolean sandbox;

    /// 杉德商户编号(mid)
    private String mid;

    /// 商户私钥证书 pfx 内容(脱敏返回)
    @SensitiveInfo(front = 6, end = 6, compact = true)
    private String merchantCertPfx;

    /// 商户私钥证书密码(脱敏返回)
    @SensitiveInfo(front = 2, end = 2)
    private String merchantCertPassword;

    /// 杉德平台公钥证书 cer 内容(脱敏返回)
    @SensitiveInfo(front = 6, end = 6, compact = true)
    private String sandCert;

    /// 市场产品编码(QZF/CSDB)
    private String marketProduct;

    /// 商户私钥证书是否已配置
    private boolean merchantCertConfigured;

    /// 商户私钥证书密码是否已配置
    private boolean merchantCertPasswordConfigured;

    /// 杉德公钥证书是否已配置
    private boolean sandCertConfigured;
}
