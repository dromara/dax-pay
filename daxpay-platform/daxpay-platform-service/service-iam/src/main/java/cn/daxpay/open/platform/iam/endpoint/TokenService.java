package cn.daxpay.open.platform.iam.endpoint;

import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.entity.UserDetail;
import cn.daxpay.open.platform.core.enums.client.ClientEnum;
import cn.daxpay.open.platform.capability.auth.authentication.AbstractAuthentication;
import cn.daxpay.open.platform.common.config.properties.PlatformStarterProperties;
import cn.daxpay.open.platform.capability.auth.entity.AuthInfoResult;
import cn.daxpay.open.platform.capability.auth.entity.LoginAuthContext;
import cn.daxpay.open.platform.capability.auth.exception.ApplicationNotFoundException;
import cn.daxpay.open.platform.capability.auth.exception.LoginFailureException;
import cn.daxpay.open.platform.capability.auth.exception.TwoFactorRequiredException;
import cn.daxpay.open.platform.capability.auth.handler.LoginFailureHandler;
import cn.daxpay.open.platform.capability.auth.handler.LoginSuccessHandler;
import cn.daxpay.open.platform.capability.auth.util.SecurityUtil;
import cn.daxpay.open.platform.iam.auth.service.twofactor.TwoFactorPreAuthService;
import cn.daxpay.open.platform.iam.result.user.UserInfoResult;
import cn.daxpay.open.platform.iam.service.twofactor.UserTwoFactorService;
import cn.daxpay.open.platform.iam.service.user.UserQueryService;
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

    private final UserTwoFactorService userTwoFactorService;

    private final TwoFactorPreAuthService twoFactorPreAuthService;

    private final UserQueryService userQueryService;

    /// 登录
    public String login(HttpServletRequest request, HttpServletResponse response) {
        AuthInfoResult authInfoResult;
        String clientCode = this.getClientCode(request);
        String loginType = SecurityUtil.getLoginType(request);
        try {
            LoginAuthContext loginAuthContext = new LoginAuthContext().setRequest(request)
                    .setResponse(response)
                    .setAuthProperties(platformStarterProperties.getAuth())
                    .setAuthLoginType(loginType)
                    .setClientCode(clientCode);
            // 校验登录终端
            this.validateClientCode(loginAuthContext);
            // 认证并获取结果
            authInfoResult = this.authentication(loginAuthContext);
            // 双因素认证: 平台已开启且用户已绑定则颁发预认证令牌, 抛出挑战异常(不计入登录失败)
            Long userId = toLong(authInfoResult.getId());
            if (userId != null && userTwoFactorService.isTwoFactorRequired(userId)) {
                String preAuthToken = twoFactorPreAuthService.create(userId, clientCode, loginType);
                String account = authInfoResult.getUserDetail() == null ? null : authInfoResult.getUserDetail().getAccount();
                throw new TwoFactorRequiredException(userId, account, preAuthToken);
            }
            // 登录处理
            this.doSaLogin(authInfoResult, clientCode, loginType);
        }
        catch (TwoFactorRequiredException e) {
            // 双因素认证挑战: 不触发失败回调, 交由全局处理器返回预认证令牌
            throw e;
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

    /// 双因素认证二次验证: 校验预认证令牌 + 动态码/备用码, 通过后建立会话
    public String secondVerify(HttpServletRequest request, HttpServletResponse response,
                               String preAuthToken, String code, String codeType) {
        TwoFactorPreAuthService.PreAuthContext context = twoFactorPreAuthService.consume(preAuthToken);
        // 预认证令牌无效或已过期
        if (context == null) {
            throw new LoginFailureException("error.auth.twoFactorPreAuthExpired");
        }
        Long userId = context.userId();
        UserInfoResult userInfoResult = userQueryService.findById(userId);
        // 校验动态码或备用码
        boolean valid;
        if ("BACKUP".equalsIgnoreCase(codeType)) {
            valid = userTwoFactorService.consumeBackupCode(userId, code);
        }
        else {
            valid = userTwoFactorService.verifyTotpCode(userId, code);
        }
        if (!valid) {
            // 双因素认证: 动态码或备用码错误
            throw new LoginFailureException(userId, userInfoResult.getAccount(), "error.auth.twoFactorCodeError");
        }
        // 恢复登录流程
        UserDetail userDetail = userInfoResult.toUserDetail();
        AuthInfoResult authInfoResult = new AuthInfoResult()
                .setId(userDetail.getId())
                .setUserDetail(userDetail);
        this.doSaLogin(authInfoResult, context.clientCode(), context.loginType());
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
            // 认证: 当前终端不支持该登录方式
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
                // 认证: 未找到对应的登录认证器
                .orElseThrow(() -> new LoginFailureException("error.auth.loginAuthenticatorNotFound"));
    }

    /// 执行 Sa-Token 登录(建立会话)
    private void doSaLogin(AuthInfoResult authInfoResult, String clientCode, String loginType) {
        var saLoginModel = new SaLoginParameter()
                .setDeviceType(clientCode)
                .setIsLastingCookie(true);

        authInfoResult.setClient(clientCode)
                .setLoginType(loginType);
        StpUtil.login(authInfoResult.getId(), saLoginModel);
        SaSession session = StpUtil.getSession();
        UserDetail userDetail = authInfoResult.getUserDetail();
        session.set(CommonCode.USER, userDetail);
    }

    /// 认证结果 id(Object) 转 Long, 无法转换返回 null
    private Long toLong(Object id) {
        if (id == null) {
            return null;
        }
        if (id instanceof Long l) {
            return l;
        }
        if (id instanceof Number n) {
            return n.longValue();
        }
        try {
            return Long.valueOf(id.toString());
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    /// 退出
    public void logout() {
        StpUtil.logout();
    }

}




