package cn.daxpay.open.channel.baofu.result.direct;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 宝付直连密钥配置返回结果
@Data
@Accessors(chain = true)
public class BaofuDirectKeyConfigResult {

    /// 通道商户号
    private String channelMchNo;

    /// 是否沙箱环境
    private Boolean sandbox;

    /// 宝付商户号(merId)
    private String merId;

    /// 宝付终端号(terId)
    private String terId;

    /// 聚合交易商户号(subMchId, 在微信/支付宝报备的商户号)
    private String subMchId;

    /// 商户证书 pfx 内容(脱敏返回)
    @SensitiveInfo(front = 6, end = 6, compact = true)
    private String merchantCertPfx;

    /// 商户证书密码(脱敏返回)
    @SensitiveInfo(front = 2, end = 2)
    private String merchantCertPassword;

    /// 宝付平台证书 cer 内容(脱敏返回)
    @SensitiveInfo(front = 6, end = 6, compact = true)
    private String platformCert;

    /// 商户证书是否已配置
    private boolean merchantCertConfigured;

    /// 商户证书密码是否已配置
    private boolean merchantCertPasswordConfigured;

    /// 平台证书是否已配置
    private boolean platformCertConfigured;
}
