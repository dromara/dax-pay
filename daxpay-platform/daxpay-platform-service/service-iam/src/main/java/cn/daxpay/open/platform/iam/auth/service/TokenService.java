package cn.daxpay.open.platform.iam.auth.service;

import cn.daxpay.open.platform.capability.auth.authentication.AuthenticationTemplate;
import cn.daxpay.open.platform.capability.auth.authentication.Authenticator;
import cn.daxpay.open.platform.capability.auth.authentication.PostAuthenticationCheck;
import cn.daxpay.open.platform.capability.auth.authentication.SecondaryAuthRequiredException;
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
import cn.dev33.satoken.stp.parameter.enums.SaLogoutMode;
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

    private final List<PostAuthenticationCheck> postAuthenticationChecks;

    private final List<LoginSuccessHandler> loginSuccessHandlers;

    private final List<LoginFailureHandler> loginFailureHandlers;

    private final UserTwoFactorService userTwoFactorService;

    private final TwoFactorPreAuthService twoFactorPreAuthService;

    private final UserQueryService userQueryService;

    private final IamSecurityConfigService iamSecurityConfigService;

    /// 登录
    public String login(HttpServletRequest request, HttpServletResponse response) {
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
            AuthInfoResult authInfoResult = this.authentication(loginAuthContext);
            // 二次验证检查后创建登录态, 密码与社交登录共用
            return this.completeAuthenticatedLogin(authInfoResult, loginAuthContext);
        }
        catch (SecondaryAuthRequiredException e) {
            // 需二次验证: 不走失败回调, 交给全局异常处理
            throw e;
        }
        catch (LoginFailureException e) {
            // 登录失败回调
            this.loginFailureHandler(request, response, e);
            throw e;
        }
    }

    /// 认证已通过后的统一收尾: 二次验证检查 → 创建登录态 → 成功回调
    ///
    /// 密码与社交登录共用, 保证超时/并发/deviceType 与双因素行为一致。
    /// 若需二次验证则抛 [SecondaryAuthRequiredException], 不创建最终登录态。
    public String completeAuthenticatedLogin(AuthInfoResult authInfoResult, LoginAuthContext context) {
        // 认证后额外检查(如双因素)
        this.applyPostAuthChecks(context, authInfoResult);
        // 创建登录态
        this.doSaLogin(authInfoResult, context.getClientCode(), context.getAuthLoginType());
        // 登录成功回调
        this.loginSuccessHandler(context.getRequest(), context.getResponse(), authInfoResult);
        return StpUtil.getTokenValue();
    }

    /// 二次验证: 校验临时凭证 + 动态码/备用码, 通过后创建登录态
    public String secondVerify(HttpServletRequest request, HttpServletResponse response,
                               String preAuthToken, String code, String codeType) {
        TwoFactorPreAuthService.PreAuthContext context = twoFactorPreAuthService.get(preAuthToken);
        // 临时凭证无效或已过期
        if (context == null) {
            // 认证: 双因素预认证已过期
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
        // 验证通过, 删除临时凭证(单次有效)
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

    /// 认证后额外检查: 任一扩展要求二次验证则抛异常(不计入登录失败)
    private void applyPostAuthChecks(LoginAuthContext context, AuthInfoResult authInfoResult) {
        for (PostAuthenticationCheck check : postAuthenticationChecks) {
            if (check.required(context, authInfoResult)) {
                throw check.createException(context, authInfoResult);
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
        Object userId = authInfoResult.getId();
        var saLoginModel = new SaLoginParameter()
                .setDeviceType(clientCode)
                .setIsLastingCookie(true)
                // 按终端隔离会话, 不共用全局 isShare
                .setIsShare(false);

        var config = iamSecurityConfigService.getSessionManagement();
        // 应用在线时长/活跃超时(对所有终端生效)
        this.applyTimeConfig(saLoginModel, config);
        // 应用并发控制(GATEWAY豁免), 返回是否需要login后按终端踢人
        boolean perDeviceKickOldest = this.applyConcurrentConfig(saLoginModel, config, userId, clientCode);

        authInfoResult.setClient(clientCode)
                .setLoginType(loginType);
        StpUtil.login(userId, saLoginModel);

        // PER_DEVICE + KICK_OLDEST 必须在 login 后执行(login前本终端尚无token)
        if (perDeviceKickOldest && config.getMaxConcurrentSessions() != null) {
            StpUtil.stpLogic.logoutByMaxLoginCount(
                    userId, null, clientCode,
                    config.getMaxConcurrentSessions(), SaLogoutMode.KICKOUT);
        }

        SaSession session = StpUtil.getSession();
        UserDetail userDetail = authInfoResult.getUserDetail();
        session.set(CommonCode.USER, userDetail);
    }

    /// 应用在线时长与活跃超时配置
    private void applyTimeConfig(SaLoginParameter model, PlatformSessionManagementConfig config) {
        if (config == null) {
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
    }

    /// 应用并发登录控制
    /// 返回 true 表示需要在 login 后手动按终端踢最早会话(PER_DEVICE + KICK_OLDEST 场景)
    private boolean applyConcurrentConfig(SaLoginParameter model, PlatformSessionManagementConfig config,
                                          Object userId, String clientCode) {
        if (config == null || !Boolean.TRUE.equals(config.getEnabled())) {
            return false;
        }
        // GATEWAY 终端豁免: 网关为机器API调用, 不受并发限制
        if (ClientEnum.GATEWAY.getCode().equals(clientCode)) {
            return false;
        }
        Integer max = config.getMaxConcurrentSessions();
        String strategy = config.getConcurrentStrategy();
        boolean perDevice = "PER_DEVICE".equals(config.getConcurrentScope());

        // NEW_SESSION 或未配并发数: 允许并发, 不限制
        if (max == null || max <= 0 || "NEW_SESSION".equals(strategy)) {
            model.setIsConcurrent(true);
            return false;
        }
        if ("KICK_OLDEST".equals(strategy)) {
            model.setIsConcurrent(true);
            if (perDevice) {
                // PER_DEVICE: setMaxLoginCount是全局的, 需login后手动按deviceType踢
                return true;
            }
            // GLOBAL: Sa-Token login内部自动注销超额最早会话
            model.setMaxLoginCount(max)
                 .setOverflowLogoutMode(SaLogoutMode.KICKOUT);
            return false;
        }
        if ("DENY_NEW".equals(strategy)) {
            // 登录前预检: PER_DEVICE按当前终端计数, GLOBAL按所有终端总和
            List<String> tokens = perDevice
                    ? StpUtil.getTokenValueListByLoginId(userId, clientCode)
                    : StpUtil.getTokenValueListByLoginId(userId);
            if (tokens.size() >= max) {
                // 认证: 超出并发登录限制
                throw new LoginFailureException("error.auth.concurrentLimitExceeded");
            }
            model.setIsConcurrent(true);
            return false;
        }
        // 未知策略: 允许并发, 不限制
        model.setIsConcurrent(true);
        return false;
    }

    /// 退出
    public void logout() {
        StpUtil.logout();
    }

}
