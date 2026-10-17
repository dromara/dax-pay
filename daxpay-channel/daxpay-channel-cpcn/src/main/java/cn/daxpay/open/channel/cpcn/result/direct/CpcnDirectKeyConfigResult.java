package cn.daxpay.open.channel.cpcn.result.direct;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 中金直连密钥配置返回结果
@Data
@Accessors(chain = true)
public class CpcnDirectKeyConfigResult {

    /// 通道商户号
    private String channelMchNo;

    /// 是否沙箱环境
    private Boolean sandbox;

    /// 中金机构编号(InstitutionID)
    private String institutionId;

    /// 中金网关地址
    private String gatewayUrl;

    /// 指定分账子商户号(多个用英文逗号分隔)
    private String appointMerchantIds;

    /// 商户证书 pfx 内容(脱敏返回)
    @SensitiveInfo(front = 6, end = 6, compact = true)
    private String merchantCertPfx;

    /// 商户证书密码(脱敏返回)
    @SensitiveInfo(front = 2, end = 2)
    private String merchantCertPassword;

    /// 中金平台证书 cer 内容(脱敏返回)
    @SensitiveInfo(front = 6, end = 6, compact = true)
    private String platformCert;

    /// 商户证书是否已配置
    private boolean merchantCertConfigured;

    /// 商户证书密码是否已配置
    private boolean merchantCertPasswordConfigured;

    /// 中金平台证书是否已配置
    private boolean platformCertConfigured;
}
