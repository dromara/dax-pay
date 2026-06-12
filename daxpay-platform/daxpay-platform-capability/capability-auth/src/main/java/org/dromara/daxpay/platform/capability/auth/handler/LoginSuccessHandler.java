package org.dromara.daxpay.platform.capability.auth.handler;

import org.dromara.daxpay.platform.capability.auth.entity.AuthInfoResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/// # 登录成功处理器接口
///
public interface LoginSuccessHandler {

    void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, AuthInfoResult authInfoResult);

}
