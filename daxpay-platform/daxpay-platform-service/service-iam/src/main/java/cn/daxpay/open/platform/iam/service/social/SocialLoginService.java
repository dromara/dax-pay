package cn.daxpay.open.platform.iam.service.social;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.daxpay.open.platform.capability.auth.authentication.UserInfoStatusCheck;
import cn.daxpay.open.platform.capability.auth.entity.AuthInfoResult;
import cn.daxpay.open.platform.capability.auth.entity.LoginAuthContext;
import cn.daxpay.open.platform.capability.auth.exception.LoginFailureException;
import cn.daxpay.open.platform.capability.auth.util.SecurityUtil;
import cn.daxpay.open.platform.capability.social.auth.SocialAuthRequestFactory;
import cn.daxpay.open.platform.capability.social.justauth.SocialAuthConfig;
import cn.daxpay.open.platform.capability.social.justauth.SocialSourceEnum;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthCallback;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthUser;
import cn.daxpay.open.platform.capability.social.justauth.request.SocialAuthRequest;
import cn.daxpay.open.platform.common.config.properties.PlatformStarterProperties;
import cn.daxpay.open.platform.core.entity.UserDetail;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.iam.entity.social.SocialLoginConfig;
import cn.daxpay.open.platform.iam.enums.SocialAuthMode;
import cn.daxpay.open.platform.iam.enums.SocialClientEnum;
import cn.daxpay.open.platform.iam.result.social.SocialBindResult;
import cn.daxpay.open.platform.iam.result.social.SocialCallbackUrlResult;
import cn.daxpay.open.platform.iam.result.social.SocialEnabledPlatformResult;
import cn.daxpay.open.platform.iam.result.social.SocialExchangeResult;
import cn.daxpay.open.platform.iam.result.user.UserInfoResult;
import cn.daxpay.open.platform.iam.service.social.other.AlipaySocialAuthRequest;
import cn.daxpay.open.platform.iam.service.social.other.AlipaySocialAuthRequestFactory;
import cn.daxpay.open.platform.iam.service.user.UserQueryService;
import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformUrlConfig;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 第三方社交登录编排服务
///
/// 编排 OAuth2 授权流程: 授权地址生成(render)、授权码兑换(login/bind)、
/// 绑定关系查询与解绑. 采用前端回调模式: 第三方平台直接重定向到前端回调页,
/// 前端拿到 code+state 后调用 exchange 接口完成换 token.
/// state 仅用于 OAuth2 合规(CSRF), 不携带业务上下文.
/// 登录和绑定的区分由回调页路由决定(两个不同的前端页面), exchange 时显式传 source + client.
/// 支付宝非标准 OAuth 经 [AlipaySocialAuthRequest] 收口, 对外与标准平台共用本服务入口.
///
@Slf4j
@Service
@RequiredArgsConstructor
public class SocialLoginService {

    /// 登录回调路径
    private static final String LOGIN_CALLBACK_PATH = "/auth/oauth-callback";

    /// 绑定回调路径
    private static final String BIND_CALLBACK_PATH = "/auth/social-bind-callback";

    private final SocialAuthRequestFactory socialAuthRequestFactory;

    private final AlipaySocialAuthRequestFactory alipaySocialAuthRequestFactory;

    private final IamUserSocialBindStore socialBindStore;

    private final IamSocialLoginHandler socialLoginHandler;

    private final SocialLoginConfigService socialLoginConfigService;

    private final PlatformUrlConfigService platformUrlConfigService;

    private final UserQueryService userQueryService;

    private final PlatformStarterProperties platformStarterProperties;

    private final List<UserInfoStatusCheck> userInfoStatusChecks;

    /// 查询已启用的第三方登录平台(登录页公开接口)
    /// 仅返回平台编码列表, 不含任何敏感字段, 供登录页动态渲染第三方登录按钮.
    public List<SocialEnabledPlatformResult> enabledList() {
        return socialLoginConfigService.findEnabledList();
    }

    /// 列出应在第三方开放平台登记的回调地址(运营/商户 × 全平台 × 登录/绑定)
    ///
    /// 完整 URL 形态: {baseUrl}/auth/oauth-callback/{source} 与 bind 路径.
    public List<SocialCallbackUrlResult> listCallbackUrls() {
        PlatformUrlConfig urlConfig = platformUrlConfigService.getUrlConfig();
        List<SocialCallbackUrlResult> list = new ArrayList<>();
        for (SocialClientEnum client : SocialClientEnum.values()) {
            String baseUrl = client.resolveBaseUrl(urlConfig);
            boolean configured = StrUtil.isNotBlank(baseUrl);
            String base = configured ? StrUtil.removeSuffix(baseUrl, "/") : "";
            for (SocialSourceEnum source : SocialSourceEnum.values()) {
                String code = source.getCode();
                list.add(buildCallbackItem(client.getCode(), code, SocialAuthMode.LOGIN, base, configured));
                list.add(buildCallbackItem(client.getCode(), code, SocialAuthMode.BIND, base, configured));
            }
        }
        return list;
    }

    private SocialCallbackUrlResult buildCallbackItem(String clientCode, String source,
                                                      SocialAuthMode mode, String base, boolean configured) {
        String path = mode == SocialAuthMode.BIND ? BIND_CALLBACK_PATH : LOGIN_CALLBACK_PATH;
        String url = configured ? base + path + "/" + source : "";
        return new SocialCallbackUrlResult()
                .setClientCode(clientCode)
                .setSource(source)
                .setMode(mode.name())
                .setUrl(url)
                .setBaseUrlConfigured(configured);
    }

    /// 生成授权地址
    /// @param source 平台来源
    /// @param client 终端编码(admin/merchant), 用于解析端点配置中的 baseUrl
    /// @param mode 授权场景(不传则按登录态判断: 已登录=绑定, 未登录=登录)
    public String generateAuthorizeUrl(String source, String client, String mode) {
        // 仅 admin/merchant
        SocialClientEnum socialClient = this.requireSocialClient(client);
        // 加载平台配置(全局唯一)
        SocialLoginConfig config = this.loadEnabledConfig(source);
        SocialSourceEnum socialSource = SocialSourceEnum.of(source);
        if (socialSource == null) {
            // 社交登录: 不支持的平台
            throw new OperationFailException("error.social.unsupportedSource");
        }
        // 按 client 解析前端 baseUrl
        String baseUrl = this.resolveBaseUrl(socialClient);
        if (StrUtil.isBlank(baseUrl)) {
            throw new OperationFailException(this.endpointMissingKey(socialClient));
        }
        SocialAuthMode authMode = this.resolveMode(mode);
        // 根据场景拼接回调基础地址
        String redirectUri = this.buildRedirectUri(baseUrl, authMode);
        // 构建授权请求
        SocialAuthConfig authConfig = socialLoginConfigService.buildAuthConfig(config, redirectUri);
        SocialAuthRequest request = this.createAuthRequest(socialSource, authConfig);
        // state 仅用于 OAuth2 合规, 不缓存业务上下文
        String state = IdUtil.fastSimpleUUID();
        return request.authorize(state);
    }

    /// OAuth 授权码兑换 - 登录场景(公开, 无需认证)
    /// 前端登录回调页收到第三方平台的 code+state 后调用此方法,
    /// 后端完成 code 换 token, 查绑定关系, 签发登录令牌.
    public SocialExchangeResult exchangeForLogin(String code, String state,
                                                  String source, String clientCode,
                                                  HttpServletRequest request, HttpServletResponse response) {
        try {
            SocialClientEnum socialClient = this.requireSocialClient(clientCode);
            String baseUrl = this.requireBaseUrl(socialClient);
            String redirectUri = this.buildRedirectUri(baseUrl, SocialAuthMode.LOGIN);
            AuthUser authUser = this.doExchange(code, state, source, redirectUri);
            // 查绑定关系
            Long userId = socialBindStore.findUserIdBySourceAndOpenId(source, authUser.getUuid()).orElse(null);
            if (userId == null) {
                // 未绑定
                return new SocialExchangeResult().setError("unbind");
            }
            // 身份域 + 用户状态检查(对齐密码路径 UserInfoStatusCheck 链)
            this.validateSocialLoginUser(userId, clientCode, source, request, response);
            // 统一建会话(内含双因素检查, TokenService.completeAuthenticatedLogin)
            String token = socialLoginHandler.login(userId, clientCode, source, request, response);
            return new SocialExchangeResult().setToken(token);
        }
        catch (LoginFailureException e) {
            // 业务拒绝或需二次验证: 原样抛出, 由全局处理器响应
            throw e;
        }
        catch (Exception e) {
            log.error("社交登录兑换失败: source={}, msg={}", source, e.getMessage(), e);
            return new SocialExchangeResult().setError("oauth_failed");
        }
    }

    /// 社交登录用户校验: client 归属 + 状态检查链
    private void validateSocialLoginUser(Long userId, String clientCode, String source,
                                         HttpServletRequest request, HttpServletResponse response) {
        UserInfoResult userInfo = userQueryService.findById(userId);
        // 绑定用户必须属于当前登录终端, 防止运营/商户串号
        if (!Objects.equals(clientCode, userInfo.getClientCode())) {
            throw new LoginFailureException(userId, userInfo.getAccount(), "error.auth.clientMismatch");
        }
        UserDetail userDetail = userInfo.toUserDetail();
        AuthInfoResult authInfoResult = new AuthInfoResult()
                .setId(userId)
                .setUserDetail(userDetail)
                .setClient(clientCode)
                .setLoginType(source);
        LoginAuthContext context = new LoginAuthContext()
                .setRequest(request)
                .setResponse(response)
                .setClientCode(clientCode)
                .setAuthLoginType(source)
                .setAuthProperties(platformStarterProperties.getAuth())
                .setUserDetail(userDetail);
        for (UserInfoStatusCheck check : userInfoStatusChecks) {
            check.check(authInfoResult, context);
        }
    }

    /// OAuth 授权码兑换 - 绑定场景(需登录)
    /// 前端绑定回调页收到第三方平台的 code+state 后调用此方法,
    /// 后端完成 code 换 token, 保存绑定关系到当前登录用户.
    public SocialExchangeResult exchangeForBind(String code, String state,
                                                 String source, String clientCode) {
        try {
            SocialClientEnum socialClient = this.requireSocialClient(clientCode);
            // 绑定场景必须已登录
            Long userId = SecurityUtil.getUserId();
            // 当前用户必须属于该身份域, 防止跨端绑定
            UserInfoResult userInfo = userQueryService.findById(userId);
            if (!Objects.equals(clientCode, userInfo.getClientCode())) {
                throw new OperationFailException("error.social.bind.clientMismatch");
            }
            String baseUrl = this.requireBaseUrl(socialClient);
            String redirectUri = this.buildRedirectUri(baseUrl, SocialAuthMode.BIND);
            AuthUser authUser = this.doExchange(code, state, source, redirectUri);
            socialBindStore.saveBind(userId, clientCode, authUser);
            return new SocialExchangeResult().setResult("bind_success");
        }
        catch (OperationFailException e) {
            throw e;
        }
        catch (Exception e) {
            log.error("社交绑定失败: source={}, msg={}", source, e.getMessage(), e);
            return new SocialExchangeResult().setError("oauth_failed");
        }
    }

    /// 查询指定用户已绑定的所有第三方账号
    public List<SocialBindResult> bindList(Long userId) {
        return socialBindStore.findBindsByUserId(userId);
    }

    /// 解除指定用户的某个平台绑定(幂等: 未绑定则无操作)
    public void unbind(Long userId, String source) {
        socialBindStore.removeBind(userId, source);
    }

    // ==================== 内部方法 ====================

    /// 共享: code 换 AuthUser(登录/绑定共用)
    private AuthUser doExchange(String code, String state, String source, String redirectUri) {
        SocialLoginConfig config = socialLoginConfigService.findEnabledBySource(source);
        if (config == null) {
            // 社交登录: 平台未配置或未启用
            throw new OperationFailException("error.social.configNotExist");
        }
        SocialAuthConfig authConfig = socialLoginConfigService.buildAuthConfig(config, redirectUri);
        SocialSourceEnum socialSource = SocialSourceEnum.of(source);
        if (socialSource == null) {
            throw new OperationFailException("error.social.unsupportedSource");
        }
        SocialAuthRequest authRequest = this.createAuthRequest(socialSource, authConfig);
        return authRequest.login(AuthCallback.of(code, state));
    }

    /// 按平台创建授权请求: 支付宝走 iam 侧 [AlipaySocialAuthRequest], 其余走 JustAuth 工厂
    private SocialAuthRequest createAuthRequest(SocialSourceEnum socialSource, SocialAuthConfig authConfig) {
        if (socialSource == SocialSourceEnum.ALIPAY) {
            return alipaySocialAuthRequestFactory.create(authConfig);
        }
        return socialAuthRequestFactory.create(socialSource, authConfig);
    }

    /// 解析授权场景(未传 mode 时按登录态判断)
    private SocialAuthMode resolveMode(String mode) {
        if (StrUtil.isNotBlank(mode)) {
            try {
                return SocialAuthMode.valueOf(mode.toUpperCase());
            } catch (IllegalArgumentException ignored) {
            }
        }
        // 默认: 已登录走绑定, 未登录走登录
        boolean login = SecurityUtil.isLogin();
        return login ? SocialAuthMode.BIND : SocialAuthMode.LOGIN;
    }

    /// 校验并解析社交登录终端(仅 admin/merchant)
    private SocialClientEnum requireSocialClient(String clientCode) {
        return SocialClientEnum.findByCode(clientCode)
                .orElseThrow(() -> new OperationFailException("error.social.unsupportedClient"));
    }

    /// 解析并校验 baseUrl 非空
    private String requireBaseUrl(SocialClientEnum socialClient) {
        String baseUrl = this.resolveBaseUrl(socialClient);
        if (StrUtil.isBlank(baseUrl)) {
            throw new OperationFailException(this.endpointMissingKey(socialClient));
        }
        return baseUrl;
    }

    /// 按 client 解析前端 baseUrl
    private String resolveBaseUrl(SocialClientEnum socialClient) {
        PlatformUrlConfig urlConfig = platformUrlConfigService.getUrlConfig();
        return socialClient.resolveBaseUrl(urlConfig);
    }

    /// 端点缺失时的 i18n key(区分运营/商户便于配置)
    private String endpointMissingKey(SocialClientEnum socialClient) {
        return socialClient == SocialClientEnum.MERCHANT
                ? "error.social.merchantEndpointNotConfigured"
                : "error.social.adminEndpointNotConfigured";
    }

    /// 构建回调基础地址(不含 source 后缀, JustAuth 层再拼 /{source})
    private String buildRedirectUri(String baseUrl, SocialAuthMode mode) {
        String base = StrUtil.removeSuffix(baseUrl, "/");
        String callbackPath = mode == SocialAuthMode.BIND
            ? BIND_CALLBACK_PATH
            : LOGIN_CALLBACK_PATH;
        return base + callbackPath;
    }

    /// 加载已启用的平台配置(不存在则抛业务异常)
    private SocialLoginConfig loadEnabledConfig(String source) {
        SocialLoginConfig config = socialLoginConfigService.findEnabledBySource(source);
        if (config == null) {
            // 社交登录: 平台未配置或未启用
            throw new OperationFailException("error.social.configNotExist");
        }
        return config;
    }
}
