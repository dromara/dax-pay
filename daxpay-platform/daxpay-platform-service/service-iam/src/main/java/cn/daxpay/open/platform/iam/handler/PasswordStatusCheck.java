package cn.daxpay.open.platform.iam.handler;

import cn.daxpay.open.platform.capability.auth.service.AccessPolicy;
import cn.daxpay.open.platform.capability.auth.util.SecurityUtil;
import cn.daxpay.open.platform.common.spring.util.WebServletUtil;
import cn.daxpay.open.platform.core.entity.UserDetail;
import cn.daxpay.open.platform.iam.exception.auth.InitialPasswordAccessException;
import cn.daxpay.open.platform.iam.exception.auth.PasswordExpiredAccessException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.List;

/// # 密码状态访问策略
///
/// 检查已登录用户密码是否过期或是否为初始密码，若是则限制接口访问(仅放行改密等白名单路径)。
/// 实现 [AccessPolicy] (而非 RouterCheck), 允许抛 [PasswordExpiredAccessException] 阻断请求。
///
@Component
public class PasswordStatusCheck implements AccessPolicy {

    private static final List<String> ALLOWED_PATHS = List.of(
            "/user/auth/update-password",
            "/user/auth/get-login-after-user-info",
            "/token/logout",
            "/captcha/",
            // 改密页拉取密码策略校验配置的必需配套
            "/platform/config/security/password-policy/validate-config"
    );

    @Override
    public void check(HttpServletRequest request, UserDetail userDetail) {
        // 超级管理员跳过密码状态检查
        if (userDetail.isAdmin()) {
            return;
        }
        if (!userDetail.needChangePassword()) {
            return;
        }
        String path = WebServletUtil.getPath();
        for (String allowedPath : ALLOWED_PATHS) {
            if (path.startsWith(allowedPath)) {
                return;
            }
        }
        // 区分初始密码与密码过期, 前端据此展示不同引导文案
        if (Boolean.TRUE.equals(userDetail.getInitialPassword())) {
            // 初始密码(管理员代设), 首次登录强制修改
            throw new InitialPasswordAccessException();
        }
        // 密码已过期, 强制改密
        throw new PasswordExpiredAccessException();
    }
}
