package cn.daxpay.open.platform.iam.auth.service;

import cn.daxpay.open.platform.capability.auth.authentication.AuthenticationChallengeException;
import cn.daxpay.open.platform.capability.auth.authentication.AuthenticationTemplate;
import cn.daxpay.open.platform.capability.auth.authentication.Authenticator;
import cn.daxpay.open.platform.capability.auth.authentication.PostAuthenticationChallenge;
import cn.daxpay.open.platform.capability.auth.handler.LoginFailureHandler;
import cn.daxpay.open.platform.capability.auth.handler.LoginSuccessHandler;
import cn.daxpay.open.platform.capability.auth.util.SecurityUtil;
import cn.daxpay.open.platform.common.config.properties.PlatformStarterProperties;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.entity.UserDetail;
import cn.daxpay.open.platform.core.enums.client.ClientEnum;
import cn.daxpay.open.platform.capability.auth.entity.AuthInfoResult;
import cn.daxpay.open.platform.capability.auth.entity.LoginAuthContext;
import cn.daxpay.open.platform.capability.auth.exception.LoginFailureException;
import cn.daxpay.open.platform.iam.auth.service.twofactor.TwoFactorPreAuthService;
import cn.daxpay.open.platform.iam.exception.auth.ApplicationNotFoundException;
import cn.daxpay.open.platform.iam.result.user.UserInfoResult;
import cn.daxpay.open.platform.iam.service.twofactor.UserTwoFactorService;
import cn.daxpay.open.platform.iam.service.user.UserQueryService;
import cn.daxpay.open.platform.system.entity.config.platform.security.PlatformSessionManagementConfig;
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

    private final List<Authenticator> authenticators;

    private final AuthenticationTemplate authenticationTemplate;

    private final List<PostAuthenticationChallenge> postAuthenticationChallenges;

    private final List<LoginSuccessHandler> loginSuccessHandlers;

    private final List<LoginFailureHandler> loginFailureHandlers;

    private final UserTwoFactorService userTwoFactorService;

    private final TwoFactorPreAuthService twoFactorPreAuthService;

    private final UserQueryService userQueryService;

    private final IamSecurityConfigService iamSecurityConfigService;

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
            // 校验该终端是否支持此种登录方式(按 clientCode + loginType 双键匹配)
            this.validateClient(loginAuthContext);
            // 认证并获取结果
            authInfoResult = this.authentication(loginAuthContext);
            // 认证后挑战(双因素/设备验证等), 任一需要则抛挑战异常(不计入登录失败)
            this.applyChallenges(loginAuthContext, authInfoResult);
            // 登录处理
            this.doSaLogin(authInfoResult, clientCode, loginType);
        }
        catch (AuthenticationChallengeException e) {
            // 挑战流程: 不触发失败回调, 交由全局处理器返回挑战结果
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
        TwoFactorPreAuthService.PreAuthContext context = twoFactorPreAuthService.get(preAuthToken);
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
        // 验证通过, 删除预认证令牌(单次有效)
        twoFactorPreAuthService.delete(preAuthToken);
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

    /// 认证后挑战: 任一挑战需要则抛出挑战异常(不计入登录失败)
    private void applyChallenges(LoginAuthContext context, AuthInfoResult authInfoResult) {
        for (PostAuthenticationChallenge challenge : postAuthenticationChallenges) {
            if (challenge.required(context, authInfoResult)) {
                throw challenge.createChallenge(context, authInfoResult);
            }
        }
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

    /// 获取并校验终端编码
    private String getClientCode(HttpServletRequest request) {
        String clientCode = SecurityUtil.getClient(request);
        ClientEnum.findByCode(clientCode)
                .orElseThrow(ApplicationNotFoundException::new);
        return clientCode;
    }

    /// 校验该终端是否支持此种登录方式(双键匹配)
    private void validateClient(LoginAuthContext loginAuthContext) {
        String clientCode = loginAuthContext.getClientCode();
        String loginType = loginAuthContext.getAuthLoginType();
        boolean supported = authenticators.stream()
                .anyMatch(auth -> auth.adaptation(clientCode, loginType));
        if (!supported) {
            // 认证: 当前终端不支持该登录方式
            throw new LoginFailureException("error.auth.loginMethodNotSupported");
        }
    }

    /// 认证(双键路由到唯一认证器, 由模板执行流程)
    private @NotNull AuthInfoResult authentication(LoginAuthContext context) {
        String clientCode = context.getClientCode();
        String loginType = context.getAuthLoginType();
        return authenticators.stream()
                .filter(auth -> auth.adaptation(clientCode, loginType))
                .findFirst()
                .map(auth -> authenticationTemplate.authenticate(auth, context))
                // 认证: 未找到对应的登录认证器
                .orElseThrow(() -> new LoginFailureException("error.auth.loginAuthenticatorNotFound"));
    }

    /// 执行 Sa-Token 登录(建立会话)
    private void doSaLogin(AuthInfoResult authInfoResult, String clientCode, String loginType) {
        var saLoginModel = new SaLoginParameter()
                .setDeviceType(clientCode)
                .setIsLastingCookie(true);
        // 应用会话管理配置(在线时长/活跃超时/并发策略)
        this.applySessionConfig(saLoginModel, authInfoResult.getId());

        authInfoResult.setClient(clientCode)
                .setLoginType(loginType);
        StpUtil.login(authInfoResult.getId(), saLoginModel);
        SaSession session = StpUtil.getSession();
        UserDetail userDetail = authInfoResult.getUserDetail();
        session.set(CommonCode.USER, userDetail);
    }

    /// 应用会话管理配置到 Sa-Token 登录参数
    /// 配置未启用时跳过, 继续使用 application.yml 中的静态默认值
    private void applySessionConfig(SaLoginParameter model, Object userId) {
        PlatformSessionManagementConfig config = iamSecurityConfigService.getSessionManagement();
        if (config == null || !Boolean.TRUE.equals(config.getEnabled())) {
            return;
        }
        // 在线时长 -> token 固定有效期(秒)
        if (config.getMaxOnlineHours() != null && config.getMaxOnlineHours() > 0) {
            model.setTimeout(config.getMaxOnlineHours() * 3600L);
        }
        // 活跃超时 -> 无操作超时(秒), 0或null表示不限制
        if (config.getActiveTimeoutHours() != null && config.getActiveTimeoutHours() > 0) {
            model.setActiveTimeout(config.getActiveTimeoutHours() * 3600L);
        }
        // 并发登录策略
        Integer max = config.getMaxConcurrentSessions();
        String strategy = config.getConcurrentStrategy();
        if ("KICK_OLDEST".equals(strategy) && max != null && max > 0) {
            // 允许并发, 超出上限时 Sa-Token 自动注销最早的会话
            model.setIsConcurrent(true).setMaxLoginCount(max);
        }
        else if ("DENY_NEW".equals(strategy) && max != null && max > 0) {
            // 登录前预检: 已达上限则拒绝新登录
            int current = StpUtil.getTokenValueListByLoginId(userId).size();
            if (current >= max) {
                throw new LoginFailureException("error.auth.concurrentLimitExceeded");
            }
            // 不设 maxLoginCount, 保持"拒绝"语义, 避免触发 Sa-Token 自动踢旧
            model.setIsConcurrent(true);
        }
        else {
            // NEW_SESSION 或未配置策略: 允许并发, 不限制数量
            model.setIsConcurrent(true);
        }
    }

    /// 退出
    public void logout() {
        StpUtil.logout();
    }

}
