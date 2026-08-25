package cn.daxpay.open.platform.core.entity;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/// 用户会话密码状态测试
class UserDetailTest {

    @Test
    void shouldRequireChangeWhenSessionPasswordExpireTimeHasPassed() {
        UserDetail userDetail = new UserDetail()
                .setPasswordExpireTime(OffsetDateTime.now(ZoneOffset.UTC).minusSeconds(1));

        assertTrue(userDetail.needChangePassword());
    }

    @Test
    void shouldNotRequireChangeWhenSessionPasswordIsStillValid() {
        UserDetail userDetail = new UserDetail()
                .setPasswordExpireTime(OffsetDateTime.now(ZoneOffset.UTC).plusDays(1));

        assertFalse(userDetail.needChangePassword());
    }

    @Test
    void shouldRequireChangeWhenPasswordIsInitial() {
        UserDetail userDetail = new UserDetail().setInitialPassword(true);

        assertTrue(userDetail.needChangePassword());
    }

    @Test
    void shouldRequireChangeWhenSessionExpiredFlagIsTrue() {
        // 会话中的过期标志(登录时密码已过期)
        UserDetail userDetail = new UserDetail().setPasswordExpired(true);

        assertTrue(userDetail.needChangePassword());
    }

    @Test
    void shouldNotRequireChangeWhenPasswordStatusAllBlank() {
        // 密码状态全空(如策略关闭时登录), 不触发强制改密
        UserDetail userDetail = new UserDetail();

        assertFalse(userDetail.needChangePassword());
    }
}
