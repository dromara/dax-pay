package cn.daxpay.open.platform.iam.handler;

import cn.daxpay.open.platform.core.entity.UserDetail;
import cn.daxpay.open.platform.iam.exception.auth.InitialPasswordAccessException;
import cn.daxpay.open.platform.iam.exception.auth.PasswordExpiredAccessException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/// # 密码状态访问策略测试
///
/// 覆盖 [PasswordStatusCheck#check]: 超级管理员绕过、白名单路径放行、
/// 初始密码(40302)与密码过期(40301)的区分拦截。
class PasswordStatusCheckTest {

    private final PasswordStatusCheck check = new PasswordStatusCheck();

    @AfterEach
    void cleanup() {
        RequestContextHolder.resetRequestAttributes();
    }

    /// 模拟当前请求路径([PasswordStatusCheck] 经 WebServletUtil 从 RequestContextHolder 取路径)
    private void mockPath(String uri) {
        MockHttpServletRequest request = new MockHttpServletRequest(null, uri);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    void check_admin_shouldBypassEvenInitialPassword() {
        // 超级管理员携带初始密码状态也应放行
        mockPath("/pay/any");
        UserDetail admin = new UserDetail().setAdmin(true).setInitialPassword(true);
        assertDoesNotThrow(() -> check.check(null, admin));
    }

    @Test
    void check_normalUserWithValidPassword_shouldPass() {
        mockPath("/pay/any");
        UserDetail normal = new UserDetail().setInitialPassword(false).setPasswordExpired(false);
        assertDoesNotThrow(() -> check.check(null, normal));
    }

    @Test
    void check_initialPassword_shouldThrow40302() {
        mockPath("/pay/any");
        UserDetail detail = new UserDetail().setInitialPassword(true);
        InitialPasswordAccessException exception = assertThrows(InitialPasswordAccessException.class,
                () -> check.check(null, detail));
        assertEquals(40302, exception.getCode());
    }

    @Test
    void check_passwordExpired_shouldThrow40301() {
        mockPath("/pay/any");
        UserDetail detail = new UserDetail().setPasswordExpired(true);
        PasswordExpiredAccessException exception = assertThrows(PasswordExpiredAccessException.class,
                () -> check.check(null, detail));
        assertEquals(40301, exception.getCode());
    }

    @Test
    void check_onlineExpireTimePassed_shouldThrow40301() {
        // 在线会话跨过过期时间: 标志为 false 但过期时间已过, 仍被拦截
        mockPath("/pay/any");
        UserDetail detail = new UserDetail()
                .setPasswordExpired(false)
                .setPasswordExpireTime(java.time.OffsetDateTime.now(java.time.ZoneOffset.UTC).minusMinutes(1));
        PasswordExpiredAccessException exception = assertThrows(PasswordExpiredAccessException.class,
                () -> check.check(null, detail));
        assertEquals(40301, exception.getCode());
    }

    @Test
    void check_whiteListPaths_shouldPassEvenInitialPassword() {
        // 强制改密期间仅放行改密、用户信息、登出、验证码与密码策略校验配置
        UserDetail detail = new UserDetail().setInitialPassword(true);
        List<String> allowedPaths = List.of(
                "/user/auth/update-password",
                "/user/auth/get-login-after-user-info",
                "/token/logout",
                "/nonce/generate",
                "/captcha/image",
                "/platform/config/security/password-policy/validate-config"
        );
        for (String path : allowedPaths) {
            mockPath(path);
            assertDoesNotThrow(() -> check.check(null, detail), "白名单路径应放行: " + path);
        }
    }
}
