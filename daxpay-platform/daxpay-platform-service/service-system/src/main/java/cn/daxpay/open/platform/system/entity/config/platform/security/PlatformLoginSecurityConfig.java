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
    /// 是否启用验证码触发（默认开启：登录失败达阈值后要求输入验证码）
    private Boolean captchaEnabled = Boolean.TRUE;
    /// 触发验证码的失败次数（默认3次，累计失败达到此次数后强制要求验证码）
    private Integer captchaTriggerAttempts = 3;
}
