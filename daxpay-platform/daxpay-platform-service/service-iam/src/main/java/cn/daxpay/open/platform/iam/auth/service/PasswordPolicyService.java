package cn.daxpay.open.platform.iam.auth.service;

import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.iam.dao.user.UserPasswordHistoryManager;
import cn.daxpay.open.platform.iam.entity.user.UserPasswordHistory;
import cn.daxpay.open.platform.system.entity.config.platform.security.PlatformPasswordPolicyConfig;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import cn.daxpay.open.platform.core.code.CommonCode;

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
        if (StrUtil.isBlank(password)) {
            // 权限: 密码不能为空
            throw new BizException(CommonCode.FAIL_CODE, "error.iam.password.notBlank");
        }
        // 禁止中文字符
        if (password.matches(".*[\\u4e00-\\u9fa5].*")) {
            // 权限: 密码不能包含中文字符
            throw new BizException(CommonCode.FAIL_CODE, "error.iam.password.noChinese");
        }
        PasswordPolicy config = this.getPolicyConfig();
        if (!config.enabled()) {
            return;
        }
        if (password.length() < config.minLength()) {
            // 权限: 密码长度不能小于N位
            throw new BizException(CommonCode.FAIL_CODE, "error.iam.password.minLength", config.minLength());
        }
        if (password.length() > config.maxLength()) {
            // 权限: 密码长度不能大于N位
            throw new BizException(CommonCode.FAIL_CODE, "error.iam.password.maxLength", config.maxLength());
        }
        if (config.requireUppercase() && password.chars().noneMatch(Character::isUpperCase)) {
            // 权限: 密码必须包含大写字母
            throw new BizException(CommonCode.FAIL_CODE, "error.iam.password.requireUppercase");
        }
        if (config.requireLowercase() && password.chars().noneMatch(Character::isLowerCase)) {
            // 权限: 密码必须包含小写字母
            throw new BizException(CommonCode.FAIL_CODE, "error.iam.password.requireLowercase");
        }
        if (config.requireDigit() && password.chars().noneMatch(Character::isDigit)) {
            // 权限: 密码必须包含数字
            throw new BizException(CommonCode.FAIL_CODE, "error.iam.password.requireDigit");
        }
        if (config.requireSpecialChar() && !this.containsSpecialChar(password, config.specialChars())) {
            // 权限: 密码必须包含特殊字符
            throw new BizException(CommonCode.FAIL_CODE, "error.iam.password.requireSpecialChar");
        }
    }

    /// 验证密码是否在历史记录中
    public void validatePasswordHistory(Long userId, String newPassword) {
        PasswordPolicy config = this.getPolicyConfig();
        if (!config.enabled() || config.historyCount() <= 0) {
            return;
        }

        List<UserPasswordHistory> histories = passwordHistoryManager.findRecentByUserId(userId, config.historyCount());
        for (UserPasswordHistory history : histories) {
            if (BCrypt.checkpw(newPassword, history.getPassword())) {
                // 权限: 新密码不能与最近N次使用过的密码相同
                throw new BizException(CommonCode.FAIL_CODE, "error.iam.password.historyDuplicate", config.historyCount());
            }
        }
    }

    /// 保存密码历史记录
    public void savePasswordHistory(Long userId, String passwordHash) {
        PasswordPolicy config = this.getPolicyConfig();
        passwordHistoryManager.addHistory(userId, passwordHash);
        if (config.historyCount() > 0) {
            passwordHistoryManager.deleteOldest(userId, config.historyCount());
        }
    }

    private PasswordPolicy getPolicyConfig() {
        PlatformPasswordPolicyConfig config = iamSecurityConfigService.getPasswordPolicy();
        // 数值字段兜底: 早期版本默认值为裸 new, 存量库可能已落全 null JSON, 拆箱前先补默认防止 NPE
        return new PasswordPolicy(
                Boolean.TRUE.equals(config.getEnabled()),
                ObjectUtil.defaultIfNull(config.getMinLength(), PlatformPasswordPolicyConfig.DEFAULT_MIN_LENGTH),
                ObjectUtil.defaultIfNull(config.getMaxLength(), PlatformPasswordPolicyConfig.DEFAULT_MAX_LENGTH),
                Boolean.TRUE.equals(config.getRequireUppercase()),
                Boolean.TRUE.equals(config.getRequireLowercase()),
                Boolean.TRUE.equals(config.getRequireDigit()),
                Boolean.TRUE.equals(config.getRequireSpecialChar()),
                StrUtil.blankToDefault(config.getSpecialChars(), PlatformPasswordPolicyConfig.DEFAULT_SPECIAL_CHARS),
                ObjectUtil.defaultIfNull(config.getHistoryCount(), PlatformPasswordPolicyConfig.DEFAULT_HISTORY_COUNT)
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

