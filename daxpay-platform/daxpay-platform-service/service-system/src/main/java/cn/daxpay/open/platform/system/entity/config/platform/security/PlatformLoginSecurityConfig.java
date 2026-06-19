package cn.daxpay.open.platform.system.entity.config.platform.security;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 登录安全配置
///
@Data
@Accessors(chain = true)
public class PlatformLoginSecurityConfig {

    /// 是否启用登录失败锁定
    private Boolean lockoutEnabled;
    /// 最大登录失败次数
    private Integer maxFailedAttempts;
    /// 锁定时长（分钟）
    private Integer lockoutDurationMinutes;
    /// 失败计数重置时长（分钟）
    private Integer failureResetMinutes;
    /// 是否启用验证码触发
    private Boolean captchaEnabled;
    /// 触发验证码的失败次数
    private Integer captchaTriggerAttempts;
}
