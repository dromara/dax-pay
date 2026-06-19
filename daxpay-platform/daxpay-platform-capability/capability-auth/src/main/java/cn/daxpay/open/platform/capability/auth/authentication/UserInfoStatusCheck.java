package cn.daxpay.open.platform.capability.auth.authentication;

import cn.daxpay.open.platform.capability.auth.entity.AuthInfoResult;
import cn.daxpay.open.platform.capability.auth.entity.LoginAuthContext;

/// # 用户状态检查
///
public interface UserInfoStatusCheck {

    /// 检查用户状态
    /// @param authInfoResult 认证返回结果
    /// @param context 登录认证上下文
    void check(AuthInfoResult authInfoResult, LoginAuthContext context);
}

