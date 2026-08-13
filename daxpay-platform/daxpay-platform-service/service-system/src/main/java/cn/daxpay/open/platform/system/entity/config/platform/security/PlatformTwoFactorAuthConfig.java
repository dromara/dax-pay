package cn.daxpay.open.platform.system.entity.config.platform.security;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 双因素认证配置
///
@Data
@Accessors(chain = true)
public class PlatformTwoFactorAuthConfig {

    /// 默认发行者名称(otpauth URI 展示用)
    public static final String DEFAULT_ISSUER = "DaxPay";
    /// 默认备用验证码数量
    public static final int DEFAULT_BACKUP_CODES_COUNT = 10;

    /// 是否启用双因素认证
    private Boolean enabled;
    /// 发行者名称
    private String issuer;
    /// 备用验证码数量
    private Integer backupCodesCount;
}
