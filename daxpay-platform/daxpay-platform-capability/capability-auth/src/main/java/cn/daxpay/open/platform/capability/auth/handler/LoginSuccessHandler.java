package cn.daxpay.open.platform.capability.auth.handler;

import cn.daxpay.open.platform.capability.auth.entity.AuthInfoResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/// # 登录成功处理器接口
///
public interface LoginSuccessHandler {

    void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, AuthInfoResult authInfoResult);

}
