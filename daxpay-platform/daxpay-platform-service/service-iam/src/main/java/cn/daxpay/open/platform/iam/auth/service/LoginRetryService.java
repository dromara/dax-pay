package cn.daxpay.open.platform.iam.auth.service;

import cn.daxpay.open.platform.core.entity.UserDetail;
import cn.daxpay.open.platform.iam.code.UserStatusEnum;
import cn.daxpay.open.platform.iam.dao.user.UserPasswordSecurityManager;
import cn.daxpay.open.platform.iam.entity.user.UserPasswordSecurity;
import cn.daxpay.open.platform.iam.result.user.PasswordStatusResult;
import cn.daxpay.open.platform.capability.auth.exception.LoginFailureException;
import cn.daxpay.open.platform.system.entity.config.platform.security.PlatformLoginSecurityConfig;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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

        // 检查失败计数是否需要重置
        this.checkAndResetFailureCount(userId, security, config);

        // 检查账号是否被锁定
        if (security.getLockTime() == null) {
            return;
        }

        OffsetDateTime lockTime = security.getLockTime();
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        if (lockTime.isBefore(now) || lockTime.isEqual(now)) {
            passwordSecurityManager.unlockAccount(userId);
            return;
        }

        long remainingMinutes = Math.max(1, java.time.Duration.between(now, lockTime).toMinutes() + 1);
        // 认证: 登录重试次数过多已锁定
        throw new LoginFailureException(userDetail.getAccount(), "error.auth.loginRetryLock", remainingMinutes);
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
        if (userId == null || StrUtil.isBlank(account)) {
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
        log.info("用户[{}]登录失败次数达到上限[{}]，锁定至[{}]", account, config.maxFailedAttempts(), lockTime);
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

        boolean expired = expireTime != null && expireTime.isBefore(now);
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
        boolean expired = expireTime != null && expireTime.isBefore(now);

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
                config.getFailureResetMinutes() == null ? 0 : config.getFailureResetMinutes(),
                config.getMaxFailedAttempts() == null ? 5 : config.getMaxFailedAttempts(),
                config.getLockoutDurationMinutes() == null ? 30 : config.getLockoutDurationMinutes()
        );
    }

    private record LoginRetryPolicyConfig(boolean lockoutEnabled,
                                          int failureResetMinutes,
                                          int maxFailedAttempts,
                                          int lockoutDurationMinutes) {
    }
}

