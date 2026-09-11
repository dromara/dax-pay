package cn.daxpay.open.platform.system.entity.config.platform.security;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 密码策略配置
///
@Data
@Accessors(chain = true)
public class PlatformPasswordPolicyConfig {

    /// 默认最小长度
    public static final int DEFAULT_MIN_LENGTH = 8;
    /// 默认最大长度
    public static final int DEFAULT_MAX_LENGTH = 32;
    /// 默认密码历史记录数量(0 表示不限制)
    public static final int DEFAULT_HISTORY_COUNT = 0;
    /// 默认过期提醒天数(剩余天数不超过该值时判定为"即将过期")
    public static final int DEFAULT_EXPIRE_WARN_DAYS = 7;
    /// 轮换周期允许的最大值(天)
    public static final int MAX_ROTATION_DAYS = 365;
    /// 过期提醒天数允许的最大值(天)
    public static final int MAX_EXPIRE_WARN_DAYS = 90;
    /// 默认特殊字符集合(策略未配置时的兜底值)
    public static final String DEFAULT_SPECIAL_CHARS = "!@#$%^&*()_+-=[]{}|;:,.<>?";

    /// 是否启用密码强度验证
    private Boolean enabled;
    /// 最小长度
    private Integer minLength;
    /// 最大长度
    private Integer maxLength;
    /// 是否要求包含大写字母
    private Boolean requireUppercase;
    /// 是否要求包含小写字母
    private Boolean requireLowercase;
    /// 是否要求包含数字
    private Boolean requireDigit;
    /// 是否要求包含特殊字符
    private Boolean requireSpecialChar;
    /// 特殊字符集合
    private String specialChars;
    /// 密码轮换周期（天）
    private Integer rotationDays;
    /// 密码过期提醒天数（剩余天数不超过该值时提示即将过期）
    private Integer expireWarnDays;
    /// 密码历史记录数量
    private Integer historyCount;
}
