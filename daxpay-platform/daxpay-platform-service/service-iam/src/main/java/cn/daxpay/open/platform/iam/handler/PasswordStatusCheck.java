package cn.daxpay.open.platform.iam.handler;

import cn.daxpay.open.platform.capability.auth.service.AccessPolicy;
import cn.daxpay.open.platform.capability.auth.util.SecurityUtil;
import cn.daxpay.open.platform.common.spring.util.WebServletUtil;
import cn.daxpay.open.platform.core.entity.UserDetail;
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
            "/captcha/"
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
        // 密码已过期或为初始密码, 强制改密
        throw new PasswordExpiredAccessException();
    }
}
