package cn.daxpay.open.platform.system.entity.config.platform.security;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 密码策略配置
///
@Data
@Accessors(chain = true)
public class PlatformPasswordPolicyConfig {

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
