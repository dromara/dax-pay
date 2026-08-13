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
    /// 密码历史记录数量
    private Integer historyCount;
}
