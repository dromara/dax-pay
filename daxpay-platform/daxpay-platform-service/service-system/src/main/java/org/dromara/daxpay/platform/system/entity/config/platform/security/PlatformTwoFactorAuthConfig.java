package org.dromara.daxpay.platform.system.entity.config.platform.security;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 双因素认证配置
///
@Data
@Accessors(chain = true)
public class PlatformTwoFactorAuthConfig {

    /// 是否启用双因素认证
    private Boolean enabled;
    /// TOTP算法类型
    private String algorithm;
    /// TOTP时间步长（秒）
    private Integer timeStep;
    /// TOTP验证码长度
    private Integer codeLength;
    /// 允许的时间窗口偏移
    private Integer timeWindowOffset;
    /// 发行者名称
    private String issuer;
    /// 备用验证码数量
    private Integer backupCodesCount;
    /// 验证码有效期（分钟）
    private Integer codeValidityMinutes;
}
