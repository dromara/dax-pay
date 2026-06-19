package cn.daxpay.open.platform.iam.handler;

import cn.daxpay.open.platform.capability.auth.exception.PasswordExpiredAccessException;
import cn.daxpay.open.platform.capability.auth.service.RouterCheck;
import cn.daxpay.open.platform.capability.auth.util.SecurityUtil;
import cn.daxpay.open.platform.core.entity.UserDetail;
import cn.daxpay.open.platform.common.spring.util.WebServletUtil;
import org.springframework.stereotype.Component;

import java.util.List;

/// # 密码状态检查
///
/// 检查用户密码是否过期或是否为初始密码，如果是则限制接口访问
@Component
public class PasswordStatusCheck implements RouterCheck {

    private static final List<String> ALLOWED_PATHS = List.of(
            "/user/auth/updatePassword",
            "/user/auth/getLoginAfterUserInfo",
            "/token/logout",
            "/captcha/"
    );

    @Override
    public int sortNo() {
        return 100;
    }

    @Override
    public boolean check(Object handler) {
        if (SecurityUtil.notLogin()) {
            return false;
        }

        UserDetail user = SecurityUtil.getUser();
        
        // 超级管理员跳过密码状态检查
        if (user.isAdmin()) {
            return false;
        }
        
        if (!user.needChangePassword()) {
            return false;
        }

        String path = WebServletUtil.getPath();
        for (String allowedPath : ALLOWED_PATHS) {
            if (path.startsWith(allowedPath)) {
                return true;
            }
        }

        throw new PasswordExpiredAccessException();
    }
}
