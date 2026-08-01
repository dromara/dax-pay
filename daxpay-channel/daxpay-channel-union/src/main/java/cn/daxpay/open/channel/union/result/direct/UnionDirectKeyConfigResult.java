package cn.daxpay.open.channel.union.result.direct;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 云闪付直连密钥配置返回结果
@Data
@Accessors(chain = true)
public class UnionDirectKeyConfigResult {

    /// 通道商户号
    private String channelMchNo;

    /// 银联商户号(merId)
    private String merId;

    /// 签名类型
    private String signType;

    /// 是否证书签名
    private boolean certSign;

    /// 应用私钥证书(脱敏返回, 保留前后各6位)
    @SensitiveInfo(front = 6, end = 6)
    private String keyPrivateCert;

    /// 私钥证书密码(脱敏返回, 保留前后各6位)
    @SensitiveInfo(front = 6, end = 6)
    private String keyPrivateCertPwd;

    /// 中级证书
    private String acpMiddleCert;

    /// 根证书
    private String acpRootCert;

    /// 是否沙箱环境
    @Schema(description = "是否沙箱环境")
    private Boolean sandbox;

    /// 私钥证书是否已配置
    private boolean keyPrivateCertConfigured;

    /// 证书密码是否已配置
    private boolean keyPrivateCertPwdConfigured;
}
