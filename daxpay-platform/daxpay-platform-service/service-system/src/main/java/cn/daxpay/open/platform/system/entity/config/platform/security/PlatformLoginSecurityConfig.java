package cn.daxpay.open.platform.system.entity.config.platform.security;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 登录安全配置
///
@Data
@Accessors(chain = true)
public class PlatformLoginSecurityConfig {

    /// 默认最大登录失败次数
    public static final int DEFAULT_MAX_FAILED_ATTEMPTS = 5;
    /// 默认锁定时长(分钟)
    public static final int DEFAULT_LOCKOUT_DURATION_MINUTES = 30;
    /// 默认失败计数重置时长(分钟, 0 表示不重置)
    public static final int DEFAULT_FAILURE_RESET_MINUTES = 0;
    /// 默认触发验证码的失败次数
    public static final int DEFAULT_CAPTCHA_TRIGGER_ATTEMPTS = 3;

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
