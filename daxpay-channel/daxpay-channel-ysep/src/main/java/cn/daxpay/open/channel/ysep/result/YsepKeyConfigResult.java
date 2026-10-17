package cn.daxpay.open.channel.ysep.result;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 银盛通道密钥配置返回结果
@Data
@Accessors(chain = true)
public class YsepKeyConfigResult {

    /// 通道商户号
    private String channelMchNo;

    /// 所属支付产品(ysep_pay / yseqt_pay)
    private String product;

    /// 发起方商户号
    private String sponsorMchNo;

    /// 收款商户号
    private String payeeMchNo;

    /// 业务代码(ysep_pay 专用)
    private String businessCode;

    /// 商户私钥证书(脱敏返回)
    @SensitiveInfo(front = 6, end = 6, compact = true)
    private String merchantCertPfx;

    /// 商户私钥证书密码(脱敏返回)
    @SensitiveInfo(front = 2, end = 2)
    private String merchantCertPassword;

    /// 银盛平台公钥证书(脱敏返回)
    @SensitiveInfo(front = 6, end = 6, compact = true)
    private String ysepayPublicCert;

    /// 商户私钥证书是否已配置
    private boolean merchantCertPfxConfigured;

    /// 商户私钥证书密码是否已配置
    private boolean merchantCertPasswordConfigured;

    /// 银盛平台公钥证书是否已配置
    private boolean ysepayPublicCertConfigured;
}
