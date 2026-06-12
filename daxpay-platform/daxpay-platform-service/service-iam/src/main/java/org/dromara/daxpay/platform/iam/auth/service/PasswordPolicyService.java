package org.dromara.daxpay.platform.iam.auth.service;

import org.dromara.daxpay.platform.core.exception.BizException;
import org.dromara.daxpay.platform.iam.dao.user.UserPasswordHistoryManager;
import org.dromara.daxpay.platform.iam.entity.user.UserPasswordHistory;
import org.dromara.daxpay.platform.system.entity.config.platform.security.PlatformPasswordPolicyConfig;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import org.dromara.daxpay.platform.core.code.CommonCode;

/// # 密码策略服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordPolicyService {

    private final IamSecurityConfigService iamSecurityConfigService;

    private final UserPasswordHistoryManager passwordHistoryManager;

    /// 验证密码强度
    public void validatePassword(String password) {
        this.validatePassword(password, null);
    }

    /// 验证密码强度
    /// @param password 密码
    /// @param isvNo 服务商号（可选）
    public void validatePassword(String password, String isvNo) {
        if (StrUtil.isBlank(password)) {
            // 密码不能为空
            throw new BizException(CommonCode.FAIL_CODE, "error.iam.password.notBlank");
        }
        // 禁止中文字符
        if (password.matches(".*[\\u4e00-\\u9fa5].*")) {
            // 密码不能包含中文字符
            throw new BizException(CommonCode.FAIL_CODE, "error.iam.password.noChinese");
        }
        PasswordPolicy config = this.getPolicyConfig(isvNo);
        if (!config.enabled()) {
            return;
        }
        if (password.length() < config.minLength()) {
            // 密码长度不能小于N位
            throw new BizException(CommonCode.FAIL_CODE, "error.iam.password.minLength", config.minLength());
        }
        if (password.length() > config.maxLength()) {
            // 密码长度不能大于N位
            throw new BizException(CommonCode.FAIL_CODE, "error.iam.password.maxLength", config.maxLength());
        }
        if (config.requireUppercase() && password.chars().noneMatch(Character::isUpperCase)) {
            // 密码必须包含大写字母
            throw new BizException(CommonCode.FAIL_CODE, "error.iam.password.requireUppercase");
        }
        if (config.requireLowercase() && password.chars().noneMatch(Character::isLowerCase)) {
            // 密码必须包含小写字母
            throw new BizException(CommonCode.FAIL_CODE, "error.iam.password.requireLowercase");
        }
        if (config.requireDigit() && password.chars().noneMatch(Character::isDigit)) {
            // 密码必须包含数字
            throw new BizException(CommonCode.FAIL_CODE, "error.iam.password.requireDigit");
        }
        if (config.requireSpecialChar() && !this.containsSpecialChar(password, config.specialChars())) {
            // 密码必须包含特殊字符
            throw new BizException(CommonCode.FAIL_CODE, "error.iam.password.requireSpecialChar");
        }
    }

    /// 验证密码是否在历史记录中
    /// @param userId 用户ID
    /// @param newPassword 新密码（明文）
    /// @param isvNo 服务商号（可选）
    public void validatePasswordHistory(Long userId, String newPassword, String isvNo) {
        PasswordPolicy config = this.getPolicyConfig(isvNo);
        if (!config.enabled() || config.historyCount() <= 0) {
            return;
        }

        List<UserPasswordHistory> histories = passwordHistoryManager.findRecentByUserId(userId, config.historyCount());
        for (UserPasswordHistory history : histories) {
            if (BCrypt.checkpw(newPassword, history.getPassword())) {
                // 新密码不能与最近N次使用过的密码相同
                throw new BizException(CommonCode.FAIL_CODE, "error.iam.password.historyDuplicate", config.historyCount());
            }
        }
    }

    /// 保存密码历史记录
    /// @param userId 用户ID
    /// @param passwordHash 密码哈希
    /// @param isvNo 服务商号（可选）
    public void savePasswordHistory(Long userId, String passwordHash, String isvNo) {
        PasswordPolicy config = this.getPolicyConfig(isvNo);
        passwordHistoryManager.addHistory(userId, passwordHash);
        if (config.historyCount() > 0) {
            passwordHistoryManager.deleteOldest(userId, config.historyCount());
        }
    }

    private PasswordPolicy getPolicyConfig(String isvNo) {
        PlatformPasswordPolicyConfig config = iamSecurityConfigService.getPasswordPolicy(isvNo);
        return new PasswordPolicy(
                Boolean.TRUE.equals(config.getEnabled()),
                config.getMinLength(),
                config.getMaxLength(),
                Boolean.TRUE.equals(config.getRequireUppercase()),
                Boolean.TRUE.equals(config.getRequireLowercase()),
                Boolean.TRUE.equals(config.getRequireDigit()),
                Boolean.TRUE.equals(config.getRequireSpecialChar()),
                config.getSpecialChars(),
                config.getHistoryCount()
        );
    }

    private boolean containsSpecialChar(String password, String specialChars) {
        return password.chars()
                .mapToObj(i -> String.valueOf((char) i))
                .anyMatch(specialChars::contains);
    }

    private record PasswordPolicy(boolean enabled,
                                  int minLength,
                                  int maxLength,
                                  boolean requireUppercase,
                                  boolean requireLowercase,
                                  boolean requireDigit,
                                  boolean requireSpecialChar,
                                  String specialChars,
                                  int historyCount) {
    }
}

