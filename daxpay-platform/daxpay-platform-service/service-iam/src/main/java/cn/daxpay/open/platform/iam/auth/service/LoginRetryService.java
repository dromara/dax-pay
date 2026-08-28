package cn.daxpay.open.platform.iam.auth.service;

import cn.daxpay.open.platform.core.entity.UserDetail;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.iam.code.UserStatusEnum;
import cn.daxpay.open.platform.iam.dao.user.UserPasswordSecurityManager;
import cn.daxpay.open.platform.iam.entity.user.UserPasswordSecurity;
import cn.daxpay.open.platform.iam.result.user.PasswordStatusResult;
import cn.daxpay.open.platform.capability.auth.exception.LoginFailureException;
import cn.daxpay.open.platform.system.entity.config.platform.security.PlatformLoginSecurityConfig;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

/// # 登录重试服务
///
/// 使用数据库存储锁定状态，支持服务商配置
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginRetryService {

    private final IamSecurityConfigService iamSecurityConfigService;

    private final UserPasswordSecurityManager passwordSecurityManager;

    /// 登录前检查
    public void checkBeforeLogin(UserDetail userDetail) {
        if (!UserStatusEnum.NORMAL.getCode().equals(userDetail.getStatus())) {
            // 认证: 用户状态异常
            throw new LoginFailureException(userDetail.getAccount(), "error.auth.userStatusError");
        }

        LoginRetryPolicyConfig config = this.getPolicyConfig();
        if (!config.lockoutEnabled()) {
            return;
        }

        Long userId = userDetail.getId();
        UserPasswordSecurity security = passwordSecurityManager.findByUserId(userId).orElse(null);
        if (security == null) {
            return;
        }

        Long remainingMinutes = this.checkLockedState(userId, security, config);
        if (remainingMinutes == null) {
            return;
        }
        // 认证: 登录重试次数过多已锁定
        throw new LoginFailureException(userDetail.getAccount(), "error.auth.loginRetryLock", remainingMinutes);
    }

    /// 敏感操作二次验证前检查: 账号处于锁定状态时拒绝操作
    ///
    /// 与登录锁定共用同一计数状态与阈值, 登录后敏感操作(修改密码/邮箱绑定解绑/通行密钥/关闭双因素等)
    /// 的密码或动态码确认前置调用, 防止持有会话的攻击者无限穷举。
    ///
    /// REQUIRES_NEW: [checkLockedState] 内的 reset/unlock UPDATE 不能把行锁留在调用方事务里 ——
    /// 调用方事务随后挂起(失败计数 [#onSensitiveVerifyFailure] 同为 REQUIRES_NEW 且 UPDATE 同一行)时,
    /// 会形成"外层事务持锁挂起、内层事务等锁"的互相等待, PG 死锁检测不可见, 无限自死锁;
    /// 且 reset(计数过期清理)/unlock(锁到期解锁) 是与业务操作结果无关的维护写, 独立提交语义正确。
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void checkBeforeSensitiveVerify(Long userId) {
        LoginRetryPolicyConfig config = this.getPolicyConfig();
        if (!config.lockoutEnabled()) {
            return;
        }
        UserPasswordSecurity security = passwordSecurityManager.findByUserId(userId).orElse(null);
        if (security == null) {
            return;
        }
        Long remainingMinutes = this.checkLockedState(userId, security, config);
        if (remainingMinutes == null) {
            return;
        }
        // 认证: 操作验证失败次数过多已锁定
        throw new BizException(CommonCode.FAIL_CODE, "error.auth.verifyRetryLock", remainingMinutes);
    }

    /// 锁定状态公共判定: 先重置过期失败计数, 再检查锁定
    /// @return 未锁定返回 null; 已锁定返回剩余分钟数(锁定期满时自动解锁并返回 null)
    private Long checkLockedState(Long userId, UserPasswordSecurity security, LoginRetryPolicyConfig config) {
        // 检查失败计数是否需要重置
        this.checkAndResetFailureCount(userId, security, config);

        // 检查账号是否被锁定
        if (security.getLockTime() == null) {
            return null;
        }

        OffsetDateTime lockTime = security.getLockTime();
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        if (lockTime.isBefore(now) || lockTime.isEqual(now)) {
            passwordSecurityManager.unlockAccount(userId);
            return null;
        }
        return Math.max(1, java.time.Duration.between(now, lockTime).toMinutes() + 1);
    }

    /// 检查并重置失败计数
    /// 如果上次失败时间超过配置的重置时长，则重置失败计数
    private void checkAndResetFailureCount(Long userId, UserPasswordSecurity security, LoginRetryPolicyConfig config) {
        if (config.failureResetMinutes() <= 0) {
            return;
        }
        if (security.getLastFailureTime() == null) {
            return;
        }
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        long minutesSinceLastFailure = ChronoUnit.MINUTES.between(security.getLastFailureTime(), now);
        if (minutesSinceLastFailure >= config.failureResetMinutes()) {
            passwordSecurityManager.resetFailureCount(userId);
            log.debug("用户[{}]失败计数已自动重置，距上次失败{}分钟", userId, minutesSinceLastFailure);
        }
    }

    /// 登录失败处理
    @Transactional(rollbackFor = Exception.class)
    public void onLoginFailure(Long userId, String account) {
        this.doFailureCount(userId, account);
    }

    /// 敏感操作二次验证失败处理: 与登录失败共用计数器与锁定策略
    ///
    /// REQUIRES_NEW: 调用方(如 [UserInfoService#updatePassword])自身事务会因验证失败异常回滚,
    /// 默认 REQUIRED 传播会把计数自增一并回滚导致锁定永不触发, 故独立事务提交。
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void onSensitiveVerifyFailure(Long userId, String account) {
        this.doFailureCount(userId, account);
    }

    /// 敏感操作二次验证成功: 清零失败计数, 与登录成功([#onLoginSuccess])同口径
    ///
    /// 计数器语义 = "连续认证失败次数", 任何一次成功认证都断开连续性。
    /// 默认 REQUIRED 传播与调用方操作同事务: 操作提交则清零生效, 操作回滚则清零一并回滚(计数保留)。
    @Transactional(rollbackFor = Exception.class)
    public void onSensitiveVerifySuccess(Long userId) {
        if (userId == null) {
            return;
        }
        passwordSecurityManager.resetPasswordErrorCount(userId);
    }

    /// 失败计数公共逻辑: 计数 + 达标锁定(登录与敏感操作共用)
    private void doFailureCount(Long userId, String account) {
        if (userId == null) {
            return;
        }

        LoginRetryPolicyConfig config = this.getPolicyConfig();
        if (!config.lockoutEnabled()) {
            return;
        }

        int errorCount = passwordSecurityManager.incrementPasswordErrorCount(userId);
        if (errorCount < config.maxFailedAttempts()) {
            return;
        }

        OffsetDateTime lockTime = OffsetDateTime.now(ZoneOffset.UTC).plusMinutes(config.lockoutDurationMinutes());
        passwordSecurityManager.lockAccount(userId, lockTime);
        // account 仅用于日志, 操作场景可能取不到, 兜底显示 userId
        log.info("用户[{}]认证失败次数达到上限[{}]，锁定至[{}]",
                StrUtil.blankToDefault(account, String.valueOf(userId)), config.maxFailedAttempts(), lockTime);
    }

    /// 登录成功处理
    /// @param userId 用户ID
    @Transactional(rollbackFor = Exception.class)
    public void onLoginSuccess(Long userId) {
        if (userId == null) {
            return;
        }
        passwordSecurityManager.resetPasswordErrorCount(userId);
    }

    /// 手动解锁账号
    /// @param userId 用户ID
    @Transactional(rollbackFor = Exception.class)
    public void unlockAccount(Long userId) {
        passwordSecurityManager.unlockAccount(userId);
        log.info("用户[{}]账号已解锁", userId);
    }

    /// 获取密码状态信息
    /// @param userId 用户ID
    /// @return 密码状态信息
    public PasswordStatusResult getPasswordStatus(Long userId) {
        UserPasswordSecurity security = passwordSecurityManager.findByUserId(userId).orElse(null);
        if (security == null) {
            return new PasswordStatusResult()
                    .setExpired(false)
                    .setExpiringSoon(false)
                    .setExpireTime(null)
                    .setInitialPassword(true);
        }

        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        OffsetDateTime expireTime = security.getPasswordExpireTime();

        boolean expired = expireTime != null && !expireTime.isAfter(now);
        boolean expiringSoon = false;
        if (expireTime != null && !expired) {
            long daysUntilExpiry = ChronoUnit.DAYS.between(now, expireTime);
            expiringSoon = daysUntilExpiry <= 7;
        }

        return new PasswordStatusResult()
                .setExpired(expired)
                .setExpiringSoon(expiringSoon)
                .setExpireTime(expireTime)
                .setInitialPassword(Boolean.TRUE.equals(security.getInitialPassword()));
    }

    /// 设置密码状态到 UserDetail
    /// @param userDetail 用户详情
    public void setPasswordStatusToUserDetail(UserDetail userDetail) {
        UserPasswordSecurity security = passwordSecurityManager.findByUserId(userDetail.getId()).orElse(null);
        if (security == null) {
            userDetail.setPasswordExpired(false);
            userDetail.setInitialPassword(true);
            userDetail.setPasswordExpireTime(null);
            return;
        }

        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        OffsetDateTime expireTime = security.getPasswordExpireTime();
        boolean expired = expireTime != null && !expireTime.isAfter(now);

        userDetail.setPasswordExpired(expired);
        userDetail.setInitialPassword(Boolean.TRUE.equals(security.getInitialPassword()));
        userDetail.setPasswordExpireTime(expireTime);
    }

    /// 获取当前错误次数
    /// @param userId 用户ID
    /// @return 错误次数
    public int getErrorCount(Long userId) {
        return passwordSecurityManager.findByUserId(userId)
                .map(security -> security.getPasswordErrorCount() == null ? 0 : security.getPasswordErrorCount())
                .orElse(0);
    }

    /// 获取登录重试策略配置
    private LoginRetryPolicyConfig getPolicyConfig() {
        PlatformLoginSecurityConfig config = iamSecurityConfigService.getLoginSecurity();
        return new LoginRetryPolicyConfig(
                Boolean.TRUE.equals(config.getLockoutEnabled()),
                ObjectUtil.defaultIfNull(config.getFailureResetMinutes(), PlatformLoginSecurityConfig.DEFAULT_FAILURE_RESET_MINUTES),
                ObjectUtil.defaultIfNull(config.getMaxFailedAttempts(), PlatformLoginSecurityConfig.DEFAULT_MAX_FAILED_ATTEMPTS),
                ObjectUtil.defaultIfNull(config.getLockoutDurationMinutes(), PlatformLoginSecurityConfig.DEFAULT_LOCKOUT_DURATION_MINUTES)
        );
    }

    private record LoginRetryPolicyConfig(boolean lockoutEnabled,
                                          int failureResetMinutes,
                                          int maxFailedAttempts,
                                          int lockoutDurationMinutes) {
    }
}

