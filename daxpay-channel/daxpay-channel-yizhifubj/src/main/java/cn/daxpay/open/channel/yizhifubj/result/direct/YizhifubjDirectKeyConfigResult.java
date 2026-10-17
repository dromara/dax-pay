package cn.daxpay.open.channel.yizhifubj.result.direct;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 一直付直连密钥配置返回结果
@Data
@Accessors(chain = true)
public class YizhifubjDirectKeyConfigResult {

    /// 通道商户号
    private String channelMchNo;

    /// 是否沙箱环境
    private Boolean sandbox;

    /// 一直付商户编号(merchantId)
    private String merchantId;

    /// 服务端ID(partnerId)
    private String partnerId;

    /// 报备序列号(reportSerialNo)
    private String reportSerialNo;

    /// 商户 pfx 证书内容(脱敏返回)
    @SensitiveInfo(front = 6, end = 6, compact = true)
    private String certPfx;

    /// 商户证书密码(脱敏返回)
    @SensitiveInfo(front = 2, end = 2)
    private String certPassword;

    /// 5upay 平台证书内容(脱敏返回)
    @SensitiveInfo(front = 6, end = 6, compact = true)
    private String platformCert;

    /// 商户 pfx 证书是否已配置
    private boolean certPfxConfigured;

    /// 商户证书密码是否已配置
    private boolean certPasswordConfigured;

    /// 5upay 平台证书是否已配置(未配置时子应用回退内置默认证书)
    private boolean platformCertConfigured;
}
