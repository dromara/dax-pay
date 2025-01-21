package cn.bootx.platform.starter.auth.handler;

import cn.bootx.platform.starter.auth.exception.LoginFailureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 登录失败处理器接口
 *
 * @author xxm
 * @since 2021/8/13
 */
public interface LoginFailureHandler {

    void onLoginFailure(HttpServletRequest request, HttpServletResponse response, LoginFailureException e);

}
