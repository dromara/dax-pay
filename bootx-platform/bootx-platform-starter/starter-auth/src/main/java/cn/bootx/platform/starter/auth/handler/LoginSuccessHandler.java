package cn.bootx.platform.starter.auth.handler;

import cn.bootx.platform.starter.auth.entity.AuthInfoResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * 登录成功处理器接口
 *
 * @author xxm
 * @since 2021/8/13
 */
public interface LoginSuccessHandler {

    void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, AuthInfoResult authInfoResult);

}
