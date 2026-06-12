package org.dromara.daxpay.platform.iam.endpoint;

import org.dromara.daxpay.platform.core.code.CommonCode;
import org.dromara.daxpay.platform.core.entity.UserDetail;
import org.dromara.daxpay.platform.core.enums.client.ClientEnum;
import org.dromara.daxpay.platform.capability.auth.authentication.AbstractAuthentication;
import org.dromara.daxpay.platform.common.config.properties.PlatformStarterProperties;
import org.dromara.daxpay.platform.capability.auth.entity.AuthInfoResult;
import org.dromara.daxpay.platform.capability.auth.entity.LoginAuthContext;
import org.dromara.daxpay.platform.capability.auth.exception.ApplicationNotFoundException;
import org.dromara.daxpay.platform.capability.auth.exception.LoginFailureException;
import org.dromara.daxpay.platform.capability.auth.handler.LoginFailureHandler;
import org.dromara.daxpay.platform.capability.auth.handler.LoginSuccessHandler;
import org.dromara.daxpay.platform.capability.auth.util.SecurityUtil;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 登录端点服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final PlatformStarterProperties platformStarterProperties;

    private final List<AbstractAuthentication> abstractAuthentications;

    private final List<LoginSuccessHandler> loginSuccessHandlers;

    private final List<LoginFailureHandler> loginFailureHandlers;

    /// 登录
    public String login(HttpServletRequest request, HttpServletResponse response) {
        AuthInfoResult authInfoResult;
        String clientCode = this.getClientCode(request);
        try {
            LoginAuthContext loginAuthContext = new LoginAuthContext().setRequest(request)
                    .setResponse(response)
                    .setAuthProperties(platformStarterProperties.getAuth())
                    .setAuthLoginType(SecurityUtil.getLoginType(request))
                    .setClientCode(clientCode);
            // 校验登录终端
            this.validateClientCode(loginAuthContext);
            // 认证并获取结果
            authInfoResult = this.authentication(loginAuthContext);
            // 登录处理（从上下文读取remember参数）
            this.login(authInfoResult, loginAuthContext);
        }
        catch (LoginFailureException e) {
            // 登录失败回调
            this.loginFailureHandler(request, response, e);
            throw e;
        }
        // 登录成功回调
        this.loginSuccessHandler(request, response, authInfoResult);
        return StpUtil.getTokenValue();
    }

    /// 成功处理
    private void loginSuccessHandler(HttpServletRequest request, HttpServletResponse response,
                                     AuthInfoResult authInfoResult) {
        for (LoginSuccessHandler loginSuccessHandler : loginSuccessHandlers) {
            try {
                loginSuccessHandler.onLoginSuccess(request, response, authInfoResult);
            }
            catch (Exception exception) {
                log.error("登录成功处理出现异常: {}", exception.getMessage(), exception);
            }
        }
    }

    /// 失败处理
    private void loginFailureHandler(HttpServletRequest request, HttpServletResponse response,
                                     LoginFailureException e) {
        for (LoginFailureHandler loginFailureHandler : loginFailureHandlers) {
            try {
                loginFailureHandler.onLoginFailure(request, response, e);
            }
            catch (Exception exception) {
                log.error("登录失败处理出现异常: {}", exception.getMessage(), exception);
            }
        }
    }

    /// 获取终端编码
    private String getClientCode(HttpServletRequest request) {
        String clientCode = SecurityUtil.getClient(request);
        ClientEnum.findByCode(clientCode)
                .orElseThrow(ApplicationNotFoundException::new);
        return clientCode;
    }

    /// 校验该终端是否支持此种登录方式
    private void validateClientCode(LoginAuthContext loginAuthContext) {
        String loginType = loginAuthContext.getAuthLoginType();
        boolean supported = abstractAuthentications.stream()
                .anyMatch(authentication -> authentication.adaptation(loginType));
        if (!supported) {
            // 当前终端不支持该登录方式
            throw new LoginFailureException("error.auth.loginMethodNotSupported");
        }
    }

    /// 认证
    private @NotNull AuthInfoResult authentication(LoginAuthContext context) {
        String loginType = context.getAuthLoginType();
        return abstractAuthentications.stream()
                .filter(o -> o.adaptation(loginType))
                .findFirst()
                .map(o -> o.authentication(context))
                // 未找到对应的登录认证器
                .orElseThrow(() -> new LoginFailureException("error.auth.loginAuthenticatorNotFound"));
    }

    /// 登录
    private void login(AuthInfoResult authInfoResult, LoginAuthContext context) {
        String authLoginType = context.getAuthLoginType();
        String clientCode = context.getClientCode();
        // 从上下文读取remember参数
        boolean remember = context.isRemember();
        var saLoginModel = new SaLoginParameter()
                .setDeviceType(clientCode)
                .setIsLastingCookie(remember);

        authInfoResult.setClient(clientCode)
                .setLoginType(authLoginType);
        StpUtil.login(authInfoResult.getId(), saLoginModel);
        SaSession session = StpUtil.getSession();
        UserDetail userDetail = authInfoResult.getUserDetail();
        session.set(CommonCode.USER, userDetail);
    }

    /// 退出
    public void logout() {
        StpUtil.logout();
    }

}




