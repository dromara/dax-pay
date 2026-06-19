package cn.daxpay.open.platform.capability.auth.handler;

import cn.daxpay.open.platform.capability.auth.exception.LoginFailureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/// # 登录失败处理器接口
///
public interface LoginFailureHandler {

    void onLoginFailure(HttpServletRequest request, HttpServletResponse response, LoginFailureException e);

}
