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

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import cn.daxpay.open.platform.core.code.CommonCode;

/// # 密码策略服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordPolicyService {

    /// 随机密码大写字母池(剔除 I / O, 避免人工转告时与 1 / 0 混淆)
    private static final char[] UPPER_CASE_POOL = "ABCDEFGHJKLMNPQRSTUVWXYZ".toCharArray();

    /// 随机密码小写字母池(剔除 i / l / o)
    private static final char[] LOWER_CASE_POOL = "abcdefghjkmnpqrstuvwxyz".toCharArray();

    /// 随机密码数字池(剔除 0 / 1)
    private static final char[] DIGIT_POOL = "23456789".toCharArray();

    /// 随机密码期望长度(在策略 [minLength, maxLength] 区间内取值)
    private static final int PREFERRED_LENGTH = 16;

    /// 生成后自校验的重试上限
    private static final int MAX_GENERATE_RETRY = 5;

    private final IamSecurityConfigService iamSecurityConfigService;

    private final UserPasswordHistoryManager passwordHistoryManager;

    private final SecureRandom secureRandom = new SecureRandom();

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

    /// 生成符合当前密码策略的随机密码
    ///
    /// 四类字符各至少包含一个(策略的 require* 均为"至少包含"式校验, 多含不违规, 可在任何开关组合下通过),
    /// 长度取期望值 16 与策略 [minLength, maxLength] 的交集, 特殊字符从策略字符集中抽取。
    /// 生成后走 [#validatePassword] 自校验, 不合规则重新生成(上限 5 次), 保证产出的密码必定可用。
    public String generateSecurePassword() {
        PasswordPolicy config = this.getPolicyConfig();
        for (int i = 0; i < MAX_GENERATE_RETRY; i++) {
            String candidate = this.doGenerate(config);
            try {
                this.validatePassword(candidate);
                return candidate;
            } catch (BizException e) {
                // 构造逻辑与校验规则同源, 正常不会触发; 兜底防止策略规则演进后生成器遗漏
                log.warn("生成的随机密码未通过策略校验, 正在重试: {}", e.getMessage());
            }
        }
        // 连续失败说明策略配置自相矛盾(如 maxLength 过小/特殊字符集无效), 属系统配置错误而非用户输入错误
        throw new IllegalStateException("随机密码生成失败, 请检查密码策略配置");
    }

    /// 按策略构造一个候选随机密码
    private String doGenerate(PasswordPolicy config) {
        // 长度: 期望 16, 夹在策略区间内, 下限 4 保证四类各占一位
        int length = Math.max(PREFERRED_LENGTH, config.minLength());
        length = Math.min(length, config.maxLength());
        length = Math.max(length, 4);
        // 特殊字符池取策略字符集, 剔除中文字符(禁中文是无条件校验)
        String specialPool = config.specialChars().chars()
                .filter(c -> c < 0x4e00 || c > 0x9fa5)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        // 每类先各取一个, 剩余位数从全池随机补齐
        List<Character> chars = new ArrayList<>(length);
        chars.add(this.pickRandom(UPPER_CASE_POOL));
        chars.add(this.pickRandom(LOWER_CASE_POOL));
        chars.add(this.pickRandom(DIGIT_POOL));
        if (!specialPool.isEmpty()) {
            chars.add(specialPool.charAt(secureRandom.nextInt(specialPool.length())));
        }
        String allPool = new String(UPPER_CASE_POOL) + new String(LOWER_CASE_POOL) + new String(DIGIT_POOL) + specialPool;
        for (int i = chars.size(); i < length; i++) {
            chars.add(allPool.charAt(secureRandom.nextInt(allPool.length())));
        }
        // Fisher-Yates 打乱, 避免前几位固定呈现"各类首字符"的分布特征
        for (int i = chars.size() - 1; i > 0; i--) {
            int j = secureRandom.nextInt(i + 1);
            char temp = chars.get(i);
            chars.set(i, chars.get(j));
            chars.set(j, temp);
        }
        StringBuilder password = new StringBuilder(length);
        chars.forEach(password::append);
        return password.toString();
    }

    /// 从字符池中随机取一个字符
    private char pickRandom(char[] pool) {
        return pool[secureRandom.nextInt(pool.length)];
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

