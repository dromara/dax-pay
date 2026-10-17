package cn.daxpay.open.channel.kuaiqian.result.direct;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 快钱直连密钥配置返回结果
@Data
@Accessors(chain = true)
public class KuaiqianDirectKeyConfigResult {

    /// 通道商户号
    private String channelMchNo;

    /// 是否沙箱环境
    private Boolean sandbox;

    /// 快钱会员号/账户号(memberCode)
    private String memberCode;

    /// 服务商快钱子账户号
    private String vendorMemberCode;

    /// 当面付商户号(merchantId)
    private String merchantId;

    /// 当面付终端号(terminalId)
    private String terminalId;

    /// 商户证书 pfx 内容(脱敏返回)
    @SensitiveInfo(front = 6, end = 6, compact = true)
    private String merchantCertPfx;

    /// 商户证书密码(脱敏返回)
    @SensitiveInfo(front = 2, end = 2)
    private String merchantCertPassword;

    /// 快钱平台证书 cer 内容(脱敏返回)
    @SensitiveInfo(front = 6, end = 6, compact = true)
    private String platformCert;

    /// SSL 客户端证书 p12 内容(脱敏返回)
    @SensitiveInfo(front = 6, end = 6, compact = true)
    private String sslCertP12;

    /// SSL 客户端证书密码(脱敏返回)
    @SensitiveInfo(front = 2, end = 2)
    private String sslCertPassword;

    /// 商户证书是否已配置
    private boolean merchantCertConfigured;

    /// 商户证书密码是否已配置
    private boolean merchantCertPasswordConfigured;

    /// 平台证书是否已配置
    private boolean platformCertConfigured;

    /// SSL 客户端证书是否已配置
    private boolean sslCertConfigured;

    /// SSL 客户端证书密码是否已配置
    private boolean sslCertPasswordConfigured;
}
