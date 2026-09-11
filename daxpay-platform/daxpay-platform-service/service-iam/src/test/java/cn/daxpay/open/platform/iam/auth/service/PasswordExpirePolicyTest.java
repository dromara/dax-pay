package cn.daxpay.open.platform.iam.auth.service;

import cn.daxpay.open.platform.core.entity.UserDetail;
import cn.daxpay.open.platform.iam.dao.user.UserPasswordSecurityManager;
import cn.daxpay.open.platform.iam.entity.user.UserPasswordSecurity;
import cn.daxpay.open.platform.iam.result.user.PasswordStatusResult;
import cn.daxpay.open.platform.system.entity.config.platform.security.PlatformPasswordPolicyConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/// # 密码轮换与过期策略测试
///
/// 覆盖 [LoginRetryService#resolveExpireTime] 的语义(启用/关闭轮换、存量补齐、人工豁免保留)、
/// [LoginRetryService#getPasswordStatus] 的只读与缺记录降级、[LoginRetryService#setPasswordStatusToUserDetail]
/// 的懒补算落库与初始密码标记。
@ExtendWith(MockitoExtension.class)
class PasswordExpirePolicyTest {

    private static final Long USER_ID = 1001L;

    @Mock
    private IamSecurityConfigService iamSecurityConfigService;

    @Mock
    private UserPasswordSecurityManager passwordSecurityManager;

    @InjectMocks
    private LoginRetryService loginRetryService;

    /// 构造密码安全记录(主键 setter 来自父类不具备链式返回, 需单独赋值)
    private UserPasswordSecurity security(OffsetDateTime lastChange, OffsetDateTime expire, Boolean initialPassword) {
        UserPasswordSecurity security = new UserPasswordSecurity();
        security.setId(USER_ID);
        security.setLastChangePasswordTime(lastChange);
        security.setPasswordExpireTime(expire);
        security.setInitialPassword(initialPassword);
        return security;
    }

    /// 平台密码策略配置(仅配置本用例关心的字段)
    private void mockPolicy(Integer rotationDays) {
        when(iamSecurityConfigService.getPasswordPolicy()).thenReturn(
                new PlatformPasswordPolicyConfig()
                        .setRotationDays(rotationDays)
                        .setExpireWarnDays(PlatformPasswordPolicyConfig.DEFAULT_EXPIRE_WARN_DAYS));
    }

    private OffsetDateTime now() {
        return OffsetDateTime.now(ZoneOffset.UTC);
    }

    @Test
    @DisplayName("未启用轮换(0/负数/为空)时忽略库内历史快照")
    void resolveExpireTime_disabledRotation_shouldIgnoreStoredValue() {
        OffsetDateTime stored = OffsetDateTime.of(2099, 12, 31, 0, 0, 0, 0, ZoneOffset.UTC);
        UserPasswordSecurity security = this.security(now().minusDays(10), stored, false);

        assertNull(LoginRetryService.resolveExpireTime(security, 0));
        assertNull(LoginRetryService.resolveExpireTime(security, -1));
        assertNull(LoginRetryService.resolveExpireTime(security, null));
    }

    @Test
    @DisplayName("已启用轮换时按最后一次改密时间补齐存量用户")
    void resolveExpireTime_enabledRotation_shouldFillFromLastChangeTime() {
        OffsetDateTime lastChange = now().minusDays(10);
        UserPasswordSecurity security = this.security(lastChange, null, false);

        assertEquals(lastChange.plusDays(90), LoginRetryService.resolveExpireTime(security, 90));
    }

    @Test
    @DisplayName("缺记录补建(无最后改密时间)时以当前时间为基准")
    void resolveExpireTime_withoutLastChangeTime_shouldUseNowAsBase() {
        UserPasswordSecurity security = this.security(null, null, false);

        OffsetDateTime resolved = LoginRetryService.resolveExpireTime(security, 30);
        assertNotNull(resolved);
        // 允许构造期间的时间抖动, 校验落在 [now+30, now+30+1分钟] 区间
        assertTrue(resolved.isAfter(now().plusDays(30).minusMinutes(1)));
        assertTrue(resolved.isBefore(now().plusDays(30).plusMinutes(1)));
    }

    @Test
    @DisplayName("库内快照更晚时保留快照(人工豁免与既有承诺不被覆盖)")
    void resolveExpireTime_storedValueLater_shouldKeepStored() {
        OffsetDateTime lastChange = now().minusDays(10);
        OffsetDateTime stored = OffsetDateTime.of(2099, 12, 31, 0, 0, 0, 0, ZoneOffset.UTC);
        UserPasswordSecurity security = this.security(lastChange, stored, false);

        assertEquals(stored, LoginRetryService.resolveExpireTime(security, 90));
    }

    @Test
    @DisplayName("库内快照更早时按策略重算(放宽立即生效, 收紧不追溯)")
    void resolveExpireTime_storedValueEarlier_shouldReCompute() {
        OffsetDateTime lastChange = now().minusDays(10);
        // 库内是旧的 10 天承诺, 当前策略 90 天 -> 以策略为准放宽
        UserPasswordSecurity security = this.security(lastChange, lastChange.plusDays(10), false);

        assertEquals(lastChange.plusDays(90), LoginRetryService.resolveExpireTime(security, 90));
    }

    @Test
    @DisplayName("缺记录的历史用户不再被判定为初始密码")
    void getPasswordStatus_withoutRecord_shouldNotTreatAsInitialPassword() {
        this.mockPolicy(90);
        when(passwordSecurityManager.findByUserId(USER_ID)).thenReturn(Optional.empty());

        PasswordStatusResult result = loginRetryService.getPasswordStatus(USER_ID);

        assertFalse(result.getInitialPassword());
        assertFalse(result.getExpired());
        assertFalse(result.getExpiringSoon());
        assertNull(result.getExpireTime());
        assertNull(result.getRemainingDays());
        assertTrue(result.getRotationEnabled());
        assertEquals(Long.valueOf(PlatformPasswordPolicyConfig.DEFAULT_EXPIRE_WARN_DAYS), result.getWarnDays());
        // 查询路径只读, 不得产生任何写入
        verify(passwordSecurityManager, never()).refreshExpireTime(any(), any());
        verify(passwordSecurityManager, never()).getOrCreateByUserId(any());
    }

    @Test
    @DisplayName("关闭轮换后在线用户立即解除限制, 且不清理库内快照")
    void getPasswordStatus_disabledRotation_shouldReleaseImmediately() {
        this.mockPolicy(0);
        OffsetDateTime stored = OffsetDateTime.of(2099, 12, 31, 0, 0, 0, 0, ZoneOffset.UTC);
        when(passwordSecurityManager.findByUserId(USER_ID))
                .thenReturn(Optional.of(this.security(now().minusDays(10), stored, false)));

        PasswordStatusResult result = loginRetryService.getPasswordStatus(USER_ID);

        assertFalse(result.getExpired());
        assertFalse(result.getRotationEnabled());
        assertNull(result.getExpireTime());
        assertNull(result.getRemainingDays());
        verify(passwordSecurityManager, never()).refreshExpireTime(any(), any());
    }

    @Test
    @DisplayName("剩余天数不超过阈值时判定为即将过期")
    void getPasswordStatus_withinWarnWindow_shouldMarkExpiringSoon() {
        this.mockPolicy(30);
        // 25 天前改密, 30 天周期 -> 剩 5 天
        OffsetDateTime lastChange = now().minusDays(25);
        when(passwordSecurityManager.findByUserId(USER_ID))
                .thenReturn(Optional.of(this.security(lastChange, null, false)));

        PasswordStatusResult result = loginRetryService.getPasswordStatus(USER_ID);

        assertFalse(result.getExpired());
        assertTrue(result.getExpiringSoon());
        // 剩余天数按天向下取整, 构造与断言之间存在毫秒级漂移, 断言落在 4~5 天区间
        assertTrue(result.getRemainingDays() >= 4 && result.getRemainingDays() <= 5,
                "剩余天数应约为 5 天, 实际: " + result.getRemainingDays());
    }

    @Test
    @DisplayName("过期时间已过时标记过期且剩余天数为 0")
    void getPasswordStatus_expired_shouldMarkExpired() {
        this.mockPolicy(30);
        OffsetDateTime lastChange = now().minusDays(40);
        when(passwordSecurityManager.findByUserId(USER_ID))
                .thenReturn(Optional.of(this.security(lastChange, lastChange.plusDays(30), false)));

        PasswordStatusResult result = loginRetryService.getPasswordStatus(USER_ID);

        assertTrue(result.getExpired());
        assertEquals(Long.valueOf(0), result.getRemainingDays());
    }

    @Test
    @DisplayName("登录时缺记录: 补建记录并按策略补算过期时间(不写初始密码标记)")
    void setPasswordStatus_withoutRecord_shouldCreateAndFillExpireTime() {
        this.mockPolicy(90);
        when(passwordSecurityManager.findByUserId(USER_ID)).thenReturn(Optional.empty());
        when(passwordSecurityManager.getOrCreateByUserId(USER_ID))
                .thenReturn(this.security(null, null, false));
        UserDetail userDetail = UserDetail.of(USER_ID, "tester", "admin", "tester", false, "normal");

        loginRetryService.setPasswordStatusToUserDetail(userDetail);

        verify(passwordSecurityManager).getOrCreateByUserId(USER_ID);
        verify(passwordSecurityManager).refreshExpireTime(eq(USER_ID), any(OffsetDateTime.class));
        assertFalse(userDetail.getInitialPassword());
        assertFalse(userDetail.needChangePassword());
        assertNotNull(userDetail.getPasswordExpireTime());
    }

    @Test
    @DisplayName("初始密码标记来自库内记录, 真初始密码仍需强制改密")
    void setPasswordStatus_initialPasswordRecord_shouldStillRequireChange() {
        this.mockPolicy(0);
        when(passwordSecurityManager.findByUserId(USER_ID))
                .thenReturn(Optional.of(this.security(now(), null, true)));
        UserDetail userDetail = UserDetail.of(USER_ID, "tester", "admin", "tester", false, "normal");

        loginRetryService.setPasswordStatusToUserDetail(userDetail);

        assertTrue(userDetail.getInitialPassword());
        assertTrue(userDetail.needChangePassword());
        // 关闭轮换时过期时间为空, 无需补算落库
        verify(passwordSecurityManager, never()).refreshExpireTime(any(), any());
    }

    @Test
    @DisplayName("过期时间已过时写入会话标记, 在线会话跨过过期点即被拦截")
    void setPasswordStatus_expired_shouldMarkPasswordExpired() {
        this.mockPolicy(30);
        OffsetDateTime lastChange = now().minusDays(40);
        when(passwordSecurityManager.findByUserId(USER_ID))
                .thenReturn(Optional.of(this.security(lastChange, lastChange.plusDays(30), false)));
        UserDetail userDetail = UserDetail.of(USER_ID, "tester", "admin", "tester", false, "normal");

        loginRetryService.setPasswordStatusToUserDetail(userDetail);

        assertTrue(userDetail.getPasswordExpired());
        assertTrue(userDetail.needChangePassword());
    }

    @Test
    @DisplayName("补算落库失败不得影响登录主流程")
    void setPasswordStatus_refreshFailure_shouldNotBreakLogin() {
        this.mockPolicy(90);
        when(passwordSecurityManager.findByUserId(USER_ID)).thenReturn(Optional.empty());
        when(passwordSecurityManager.getOrCreateByUserId(USER_ID))
                .thenReturn(this.security(null, null, false));
        // 返回值 void 的方法不能用 when(...) 打桩, 需走 doThrow
        doThrow(new RuntimeException("db unavailable"))
                .when(passwordSecurityManager).refreshExpireTime(any(), any());
        UserDetail userDetail = UserDetail.of(USER_ID, "tester", "admin", "tester", false, "normal");

        assertDoesNotThrow(() -> loginRetryService.setPasswordStatusToUserDetail(userDetail));
        assertNotNull(userDetail.getPasswordExpireTime());
    }
}
