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
import cn.daxpay.open.platform.iam.result.social.SocialBindScanQrResult;
import cn.daxpay.open.platform.iam.result.social.SocialBindScanStatusResult;
import cn.daxpay.open.platform.iam.result.social.SocialCallbackUrlResult;
import cn.daxpay.open.platform.iam.result.social.SocialEnabledPlatformResult;
import cn.daxpay.open.platform.iam.result.social.SocialExchangeResult;
import cn.daxpay.open.platform.iam.result.social.SocialLoginScanQrResult;
import cn.daxpay.open.platform.iam.result.social.SocialLoginScanStatusResult;
import cn.daxpay.open.platform.iam.result.user.UserInfoResult;
import cn.daxpay.open.platform.iam.service.social.other.AlipaySocialAuthRequest;
import cn.daxpay.open.platform.iam.service.social.other.AlipaySocialAuthRequestFactory;
import cn.daxpay.open.platform.iam.service.user.UserQueryService;
import cn.daxpay.open.platform.system.entity.config.platform.auth.PlatformWechatMpAuthConfig;
import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformUrlConfig;
import cn.daxpay.open.platform.system.service.config.auth.PlatformWechatMpAuthConfigService;
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
/// ## 跳转型平台回调走支付网关透传
/// 跳转型平台(支付宝/微信公众号)与支付认证共用同一份开放平台凭据, 回调名额竞争激烈
/// (支付宝授权回调地址唯一且域名级校验需显式开启), 因此登录/绑定回调统一落在
/// 支付网关域名的透传路径(`/auth/{oauth|social-bind}-callback/{client}/{source}`),
/// 由网关 H5 透传页凭 [resolveTransitTarget] 跳回对应端原有回调页, 各端页面与 exchange 接口零改动.
///
@Slf4j
@Service
@RequiredArgsConstructor
public class SocialLoginService {

    /// 登录回调路径
    private static final String LOGIN_CALLBACK_PATH = "/auth/oauth-callback";

    /// 绑定回调路径
    private static final String BIND_CALLBACK_PATH = "/auth/social-bind-callback";

    /// 扫码绑定回调路径(公众号跨设备绑定专用, 落在网关域名的 H5 回调页, 不经透传页)
    private static final String SCAN_BIND_CALLBACK_PATH = "/social/bind-scan-callback";

    /// 扫码绑定 H5 发起页路径(二维码内容, 手机微信扫码打开)
    private static final String SCAN_BIND_PAGE_PATH = "/social/bind-scan";

    /// 扫码登录回调路径(公众号跨设备登录专用, 落在网关域名的 H5 回调页, 不经透传页)
    private static final String SCAN_LOGIN_CALLBACK_PATH = "/social/login-scan-callback";

    /// 扫码登录 H5 发起页路径(二维码内容, 手机微信扫码打开)
    private static final String SCAN_LOGIN_PAGE_PATH = "/social/login-scan";

    private final SocialAuthRequestFactory socialAuthRequestFactory;

    private final AlipaySocialAuthRequestFactory alipaySocialAuthRequestFactory;

    private final IamUserSocialBindStore socialBindStore;

    private final SocialBindScanStore socialBindScanStore;

    private final SocialLoginScanStore socialLoginScanStore;

    private final IamSocialLoginHandler socialLoginHandler;

    private final SocialLoginConfigService socialLoginConfigService;

    private final PlatformUrlConfigService platformUrlConfigService;

    private final PlatformWechatMpAuthConfigService platformWechatMpAuthConfigService;

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
    /// 标准型平台 URL 形态: {baseUrl}/auth/oauth-callback/{source} 与 bind 路径.
    /// 跳转型平台: 回调走支付网关域名的透传路径(带 client 段), 各端只需在开放平台登记网关域名.
    public List<SocialCallbackUrlResult> listCallbackUrls() {
        PlatformUrlConfig urlConfig = platformUrlConfigService.getUrlConfig();
        List<SocialCallbackUrlResult> list = new ArrayList<>();
        for (SocialClientEnum client : SocialClientEnum.values()) {
            String baseUrl = client.resolveBaseUrl(urlConfig);
            boolean configured = StrUtil.isNotBlank(baseUrl);
            String base = configured ? StrUtil.removeSuffix(baseUrl, "/") : "";
            for (SocialSourceEnum source : SocialSourceEnum.values()) {
                String code = source.getCode();
                if (source.isPlatformRedirect()) {
                    // 跳转型: 网关透传路径, 网关未配置时 URL 置空
                    String gatewayBase = StrUtil.removeSuffix(urlConfig.getPaymentGatewayBaseUrl(), "/");
                    boolean gatewayConfigured = StrUtil.isNotBlank(gatewayBase);
                    String transitBase = gatewayConfigured ? gatewayBase + "/" + client.getCode() : "";
                    list.add(buildCallbackItem(client.getCode(), code, SocialAuthMode.LOGIN, transitBase, gatewayConfigured));
                    list.add(buildCallbackItem(client.getCode(), code, SocialAuthMode.BIND, transitBase, gatewayConfigured));
                } else {
                    list.add(buildCallbackItem(client.getCode(), code, SocialAuthMode.LOGIN, base, configured));
                    list.add(buildCallbackItem(client.getCode(), code, SocialAuthMode.BIND, base, configured));
                }
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
    /// @param client 身份域编码(admin/merchant), 用于解析端点配置中的 baseUrl
    /// @param mode 授权场景(不传则按登录态判断: 已登录=绑定, 未登录=登录)
    public String generateAuthorizeUrl(String source, String client, String mode) {
        return this.generateAuthorizeUrl(source, client, mode, null);
    }

    /// 生成授权地址
    /// @param silent true=应用内静默/网页授权(企微 oauth / 钉钉免确认 / 飞书应用内授权)
    public String generateAuthorizeUrl(String source, String client, String mode, Boolean silent) {
        // 仅 admin/merchant
        SocialClientEnum socialClient = this.requireSocialClient(client);
        // 加载平台配置(全局唯一)
        SocialLoginConfig config = this.loadEnabledConfig(source);
        SocialSourceEnum socialSource = SocialSourceEnum.of(source);
        if (Objects.isNull(socialSource)) {
            // 社交登录: 不支持的平台
            throw new OperationFailException("error.social.unsupportedSource");
        }
        // 按 client 解析前端 baseUrl
        String baseUrl = this.resolveBaseUrl(socialClient);
        if (StrUtil.isBlank(baseUrl)) {
            // 社交登录: 运营/商户端前端地址未配置
            throw new OperationFailException(this.endpointMissingKey(socialClient));
        }
        SocialAuthMode authMode = this.resolveMode(mode);
        // 根据场景拼接回调基础地址(跳转型平台走支付网关域名的透传路径)
        String redirectUri = this.buildRedirectUri(baseUrl, socialSource, socialClient, authMode);
        // 构建授权请求
        SocialAuthConfig authConfig = socialLoginConfigService.buildAuthConfig(config, redirectUri);
        // 公众号: 凭据来自平台级配置, 不写在 iam_social_login_config
        this.fillPlatformRedirectCredentials(socialSource, authConfig);
        // 应用内自动登录: 企微走网页授权、钉钉省略 prompt=consent(其余平台沿用默认授权地址)
        if (Boolean.TRUE.equals(silent)) {
            authConfig.setSilent(true);
        }
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
            AuthUser authUser = this.doExchange(code, state, source, socialClient, SocialAuthMode.LOGIN);
            // 查绑定关系
            Long userId = socialBindStore.findUserIdBySourceAndOpenId(source, authUser.getUuid()).orElse(null);
            if (Objects.isNull(userId)) {
                // 未绑定
                return new SocialExchangeResult().setError("unbind");
            }
            // 身份域 + 用户状态检查(对齐密码路径 UserInfoStatusCheck 链)
            this.validateSocialLoginUser(userId, clientCode, source, request, response);
            // 统一创建登录态(内含双因素检查, TokenService.completeAuthenticatedLogin)
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
            // 社交登录: 绑定用户不属于当前终端
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
                // 社交登录: 绑定用户不属于当前终端
                throw new OperationFailException("error.social.bind.clientMismatch");
            }
            AuthUser authUser = this.doExchange(code, state, source, socialClient, SocialAuthMode.BIND);
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

    // ==================== 公众号扫码绑定(跨设备) ====================

    /// 生成扫码绑定二维码(PC 端发起, 需登录)
    ///
    /// 公众号网页授权仅微信客户端内可完成, PC 浏览器直接发起必被微信拒绝;
    /// 本接口生成短期票据并返回含票据的 H5 页面地址, 由前端转二维码供手机微信扫码.
    /// 票据 5 分钟过期, 绑定结果经 [#queryBindScanStatus] 轮询获取.
    public SocialBindScanQrResult createBindScanTicket(String source, String client) {
        SocialSourceEnum socialSource = SocialSourceEnum.of(source);
        if (Objects.isNull(socialSource) || !socialSource.isWechatOnly()) {
            // 仅公众号受微信客户端限制, 其余平台 PC 可直接发起绑定
            throw new OperationFailException("error.social.bind.scanNotSupported");
        }
        this.requireSocialClient(client);
        Long userId = SecurityUtil.getUserId();
        // 当前用户必须属于该身份域, 防止跨端绑定(对齐 [exchangeForBind] 的校验)
        UserInfoResult userInfo = userQueryService.findById(userId);
        if (!Objects.equals(client, userInfo.getClientCode())) {
            throw new OperationFailException("error.social.bind.clientMismatch");
        }
        // 预校验配置(平台启用/公众号凭据/网关地址), 避免二维码生成后手机端才发现配置缺失;
        // 传入临时空配置仅为触发 [fillPlatformRedirectCredentials] 内的凭据完整性校验
        this.loadEnabledConfig(source);
        this.fillPlatformRedirectCredentials(socialSource, new SocialAuthConfig());
        String gatewayBase = this.requireGatewayBase();
        String ticket = IdUtil.fastSimpleUUID();
        socialBindScanStore.saveWaiting(ticket, userId, client, source);
        String qrUrl = gatewayBase + SCAN_BIND_PAGE_PATH + "?ticket=" + ticket;
        return new SocialBindScanQrResult().setTicket(ticket).setQrUrl(qrUrl);
    }

    /// 生成扫码绑定的微信授权地址(手机端 H5 发起页调用, 凭票据定位待绑定用户)
    ///
    /// state 仍为纯 UUID 零编码, 经 Redis 一次性映射回票据(见 [SocialBindScanStore#saveState]),
    /// 授权回调直接落网关域名的扫码绑定回调页(不经透传页, 与登录/普通绑定回调互不干扰).
    public String buildBindScanAuthUrl(String ticket) {
        SocialBindScanStore.BindScanContext context = this.requireScanContext(ticket);
        SocialSourceEnum socialSource = SocialSourceEnum.of(context.getSource());
        String state = IdUtil.fastSimpleUUID();
        socialBindScanStore.saveState(state, ticket);
        return this.buildScanAuthorizeUrl(socialSource, context.getClientCode(), state, SCAN_BIND_CALLBACK_PATH);
    }

    /// 扫码绑定的授权码兑换(手机端回调页调用)
    ///
    /// state 一次性映射换取票据, 凭票据中的用户身份完成绑定(手机端无登录态);
    /// 兑换成功后将票据标记为 success, PC 端轮询到该状态后刷新绑定列表.
    public SocialExchangeResult exchangeBindScan(String code, String state) {
        try {
            String ticket = socialBindScanStore.consumeTicketByState(state);
            SocialBindScanStore.BindScanContext context = Objects.isNull(ticket) ? null : socialBindScanStore.get(ticket);
            if (Objects.isNull(context)) {
                // state 映射或票据已过期
                throw new OperationFailException("error.social.bind.scanTicketInvalid");
            }
            SocialSourceEnum socialSource = SocialSourceEnum.of(context.getSource());
            // 回调地址与发起侧保持一致(扫码绑定走网关独立回调路径)
            String redirectUri = this.requireGatewayBase() + SCAN_BIND_CALLBACK_PATH;
            AuthUser authUser = this.doExchange(code, state, context.getSource(),
                SocialClientEnum.findByCode(context.getClientCode()).orElse(null), SocialAuthMode.BIND, redirectUri);
            socialBindStore.saveBind(context.getUserId(), context.getClientCode(), authUser);
            socialBindScanStore.markSuccess(ticket, context);
            return new SocialExchangeResult().setResult("bind_success");
        }
        catch (OperationFailException e) {
            throw e;
        }
        catch (Exception e) {
            log.error("扫码绑定失败: msg={}", e.getMessage(), e);
            return new SocialExchangeResult().setError("oauth_failed");
        }
    }

    /// 查询扫码绑定结果(PC 端轮询, 需登录)
    ///
    /// 票据不存在、已过期或不属于当前用户时统一返回 not_exist(防票据遍历探测).
    public SocialBindScanStatusResult queryBindScanStatus(String ticket) {
        SocialBindScanStore.BindScanContext context = socialBindScanStore.get(ticket);
        Long userId = SecurityUtil.getUserId();
        String status = Objects.isNull(context) || !Objects.equals(userId, context.getUserId())
            ? SocialBindScanStatusResult.NOT_EXIST
            : context.getStatus();
        return new SocialBindScanStatusResult().setStatus(status);
    }

    /// 查询扫码票据上下文, 不存在或已过期抛业务异常
    private SocialBindScanStore.BindScanContext requireScanContext(String ticket) {
        SocialBindScanStore.BindScanContext context = socialBindScanStore.get(ticket);
        if (Objects.isNull(context)) {
            throw new OperationFailException("error.social.bind.scanTicketInvalid");
        }
        return context;
    }

    // ==================== 公众号扫码登录(跨设备) ====================

    /// 生成扫码登录二维码(PC 登录页匿名发起)
    ///
    /// 登录场景 PC 端无登录态, 票据不携带用户身份; 手机端授权兑换后按绑定关系回填用户,
    /// PC 轮询到 success 时凭票据一次性换取登录令牌. 票据 5 分钟过期.
    public SocialLoginScanQrResult createLoginScanTicket(String source, String client) {
        SocialSourceEnum socialSource = SocialSourceEnum.of(source);
        if (Objects.isNull(socialSource) || !socialSource.isWechatOnly()) {
            // 仅公众号受微信客户端限制, 其余平台 PC 可直接跳转授权
            throw new OperationFailException("error.social.login.scanNotSupported");
        }
        this.requireSocialClient(client);
        // 预校验配置(平台启用/公众号凭据/网关地址), 避免二维码生成后手机端才发现配置缺失;
        // 传入临时空配置仅为触发 [fillPlatformRedirectCredentials] 内的凭据完整性校验
        this.loadEnabledConfig(source);
        this.fillPlatformRedirectCredentials(socialSource, new SocialAuthConfig());
        String gatewayBase = this.requireGatewayBase();
        String ticket = IdUtil.fastSimpleUUID();
        socialLoginScanStore.saveWaiting(ticket, client, source);
        String qrUrl = gatewayBase + SCAN_LOGIN_PAGE_PATH + "?ticket=" + ticket;
        return new SocialLoginScanQrResult().setTicket(ticket).setQrUrl(qrUrl);
    }

    /// 生成扫码登录的微信授权地址(手机端 H5 发起页调用, 凭票据定位身份域与平台)
    ///
    /// state 仍为纯 UUID 零编码, 经 Redis 一次性映射回票据(见 [SocialLoginScanStore#saveState]),
    /// 授权回调直接落网关域名的扫码登录回调页(与扫码绑定回调路径分离, 互不干扰).
    public String buildLoginScanAuthUrl(String ticket) {
        SocialLoginScanStore.LoginScanContext context = this.requireLoginScanContext(ticket);
        SocialSourceEnum socialSource = SocialSourceEnum.of(context.getSource());
        String state = IdUtil.fastSimpleUUID();
        socialLoginScanStore.saveState(state, ticket);
        return this.buildScanAuthorizeUrl(socialSource, context.getClientCode(), state, SCAN_LOGIN_CALLBACK_PATH);
    }

    /// 扫码登录的授权码兑换(手机端回调页调用)
    ///
    /// state 一次性映射换取票据; 授权微信未绑定账号时标记 not_bound 并引导先用账号密码
    /// 登录后绑定(不自动注册); 已绑定则在手机端请求上下文完成用户校验(身份域归属 + 状态
    /// 检查链, 失败原因直接透出到手机页), 通过后标记 success 待 PC 轮询签发登录态.
    public SocialExchangeResult exchangeLoginScan(String code, String state,
                                                  HttpServletRequest request, HttpServletResponse response) {
        try {
            String ticket = socialLoginScanStore.consumeTicketByState(state);
            SocialLoginScanStore.LoginScanContext context = Objects.isNull(ticket) ? null : socialLoginScanStore.get(ticket);
            if (Objects.isNull(context)) {
                // state 映射或票据已过期
                throw new OperationFailException("error.social.login.scanTicketInvalid");
            }
            SocialSourceEnum socialSource = SocialSourceEnum.of(context.getSource());
            // 回调地址与发起侧保持一致(扫码登录走网关独立回调路径)
            String redirectUri = this.requireGatewayBase() + SCAN_LOGIN_CALLBACK_PATH;
            AuthUser authUser = this.doExchange(code, state, context.getSource(),
                SocialClientEnum.findByCode(context.getClientCode()).orElse(null), SocialAuthMode.LOGIN, redirectUri);
            Long userId = socialBindStore.findUserIdBySourceAndOpenId(context.getSource(), authUser.getUuid()).orElse(null);
            if (Objects.isNull(userId)) {
                // 未绑定: 不自动注册, 手机端引导用账号密码登录后在个人中心绑定
                socialLoginScanStore.markNotBound(ticket, context);
                return new SocialExchangeResult().setError("unbind");
            }
            this.validateSocialLoginUser(userId, context.getClientCode(), context.getSource(), request, response);
            socialLoginScanStore.markSuccess(ticket, context.setUserId(userId));
            return new SocialExchangeResult().setResult("login_ready");
        }
        catch (OperationFailException | LoginFailureException e) {
            throw e;
        }
        catch (Exception e) {
            log.error("扫码登录兑换失败: msg={}", e.getMessage(), e);
            return new SocialExchangeResult().setError("oauth_failed");
        }
    }

    /// 查询扫码登录结果(PC 登录页匿名轮询)
    ///
    /// 票据不存在或已过期统一返回 not_exist; success 状态一次性消费(读取即删除),
    /// 在 PC 轮询请求的上下文签发登录令牌——双因素等登录拦截以业务异常透出(40101+
    /// preAuthToken, 与 exchange-login 同协议), 由前端进入二次验证面板.
    public SocialLoginScanStatusResult queryLoginScanStatus(String ticket,
                                                            HttpServletRequest request, HttpServletResponse response) {
        SocialLoginScanStore.LoginScanContext context = socialLoginScanStore.get(ticket);
        if (Objects.isNull(context)) {
            return new SocialLoginScanStatusResult().setStatus(SocialLoginScanStatusResult.NOT_EXIST);
        }
        if (Objects.equals(context.getStatus(), SocialLoginScanStore.STATUS_NOT_BOUND)) {
            return new SocialLoginScanStatusResult().setStatus(SocialLoginScanStore.STATUS_NOT_BOUND);
        }
        if (Objects.equals(context.getStatus(), SocialLoginScanStore.STATUS_CANCELLED)) {
            return new SocialLoginScanStatusResult().setStatus(SocialLoginScanStatusResult.CANCELLED);
        }
        if (!Objects.equals(context.getStatus(), SocialLoginScanStore.STATUS_SUCCESS)) {
            return new SocialLoginScanStatusResult().setStatus(SocialLoginScanStore.STATUS_WAITING);
        }
        // 一次性消费: 并发轮询只有一方拿到上下文并签发令牌
        SocialLoginScanStore.LoginScanContext consumed = socialLoginScanStore.consume(ticket);
        if (Objects.isNull(consumed) || Objects.isNull(consumed.getUserId())) {
            return new SocialLoginScanStatusResult().setStatus(SocialLoginScanStatusResult.NOT_EXIST);
        }
        String token = socialLoginHandler.login(consumed.getUserId(), consumed.getClientCode(), consumed.getSource(), request, response);
        return new SocialLoginScanStatusResult().setStatus(SocialLoginScanStore.STATUS_SUCCESS).setToken(token);
    }

    /// 取消扫码登录(手机端确认页取消按钮调用, 匿名)
    ///
    /// 微信授权页仅首次授权时弹出, 不构成按次确认; 手机端确认页是跨设备扫码登录
    /// 唯一的按次用户同意环节, 取消后拒绝为 PC 端签发登录态.
    /// 仅 waiting 状态可取消(授权已完成或票据不存在时为空操作, 不暴露票据状态).
    public void cancelLoginScan(String ticket) {
        SocialLoginScanStore.LoginScanContext context = socialLoginScanStore.get(ticket);
        if (Objects.isNull(context) || !Objects.equals(context.getStatus(), SocialLoginScanStore.STATUS_WAITING)) {
            return;
        }
        socialLoginScanStore.markCancelled(ticket, context);
    }

    /// 查询扫码登录票据上下文, 不存在或已过期抛业务异常
    private SocialLoginScanStore.LoginScanContext requireLoginScanContext(String ticket) {
        SocialLoginScanStore.LoginScanContext context = socialLoginScanStore.get(ticket);
        if (Objects.isNull(context)) {
            throw new OperationFailException("error.social.login.scanTicketInvalid");
        }
        return context;
    }

    /// 构建扫码授权地址(回调走网关域名独立路径, 绕开透传页; 绑定/登录按各自回调路径)
    private String buildScanAuthorizeUrl(SocialSourceEnum socialSource, String clientCode, String state, String redirectPath) {
        SocialLoginConfig config = this.loadEnabledConfig(socialSource.getCode());
        String redirectUri = this.requireGatewayBase() + redirectPath;
        SocialAuthConfig authConfig = socialLoginConfigService.buildAuthConfig(config, redirectUri);
        // 公众号: 凭据来自平台级配置
        this.fillPlatformRedirectCredentials(socialSource, authConfig);
        SocialAuthRequest request = this.createAuthRequest(socialSource, authConfig);
        return request.authorize(state);
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

    /// 共享: code 换 AuthUser(登录/绑定共用), redirectUri 拼装内聚于此保证与发起侧一致
    private AuthUser doExchange(String code, String state, String source,
                                SocialClientEnum socialClient, SocialAuthMode mode) {
        return this.doExchange(code, state, source, socialClient, mode, null);
    }

    /// @param redirectUriOverride 非空时覆盖默认回调地址拼装(扫码绑定走网关独立回调路径)
    private AuthUser doExchange(String code, String state, String source,
                                SocialClientEnum socialClient, SocialAuthMode mode, String redirectUriOverride) {
        SocialLoginConfig config = socialLoginConfigService.findEnabledBySource(source);
        if (Objects.isNull(config)) {
            // 社交登录: 平台未配置或未启用
            throw new OperationFailException("error.social.configNotExist");
        }
        SocialSourceEnum socialSource = SocialSourceEnum.of(source);
        if (Objects.isNull(socialSource)) {
            // 社交登录: 不支持的平台
            throw new OperationFailException("error.social.unsupportedSource");
        }
        String redirectUri;
        if (StrUtil.isNotBlank(redirectUriOverride)) {
            redirectUri = redirectUriOverride;
        }
        else {
            String baseUrl = this.requireBaseUrl(socialClient);
            redirectUri = this.buildRedirectUri(baseUrl, socialSource, socialClient, mode);
        }
        SocialAuthConfig authConfig = socialLoginConfigService.buildAuthConfig(config, redirectUri);
        // 公众号: 凭据来自平台级配置
        this.fillPlatformRedirectCredentials(socialSource, authConfig);
        SocialAuthRequest authRequest = this.createAuthRequest(socialSource, authConfig);
        return authRequest.login(AuthCallback.of(code, state));
    }

    /// 平台级跳转型凭据回填(公众号 AppId/Secret 等)
    private void fillPlatformRedirectCredentials(SocialSourceEnum socialSource, SocialAuthConfig authConfig) {
        if (socialSource != SocialSourceEnum.WECHAT_MP) {
            return;
        }
        PlatformWechatMpAuthConfig mp = platformWechatMpAuthConfigService.getWechatMpAuthConfig();
        if (Objects.isNull(mp) || StrUtil.isBlank(mp.getAppId()) || StrUtil.isBlank(mp.getAppSecret())) {
            // 社交登录: 微信公众号配置不完整
            throw new OperationFailException("error.social.wechatMpNotConfigured");
        }
        authConfig.setClientId(mp.getAppId());
        authConfig.setClientSecret(mp.getAppSecret());
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
                .orElseThrow(() -> {
                    // 社交登录: 不支持的身份域编码(仅 admin/merchant)
                    throw new OperationFailException("error.social.unsupportedClient");
                });
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
    ///
    /// 跳转型平台(支付宝/公众号): 回调走支付网关域名的透传路径, 路径带 client 段供
    /// H5 透传页识别目标端, 解决同一 AppId 下登录回调与支付认证回调竞争开放平台回调名额的问题.
    /// 标准型平台: 沿用各端自身域名的回调路径.
    private String buildRedirectUri(String baseUrl, SocialSourceEnum socialSource,
                                    SocialClientEnum socialClient, SocialAuthMode mode) {
        String callbackPath = mode == SocialAuthMode.BIND
            ? BIND_CALLBACK_PATH
            : LOGIN_CALLBACK_PATH;
        if (socialSource.isPlatformRedirect()) {
            return this.requireGatewayBase() + callbackPath + "/" + socialClient.getCode();
        }
        return StrUtil.removeSuffix(baseUrl, "/") + callbackPath;
    }

    /// 校验并返回支付网关前端地址(去尾斜杠), 未配置时抛业务异常
    private String requireGatewayBase() {
        String gatewayBase = platformUrlConfigService.getUrlConfig().getPaymentGatewayBaseUrl();
        if (StrUtil.isBlank(gatewayBase)) {
            // 社交登录: 跳转型平台回调依赖支付网关域名, 未配置时无法生成授权地址
            throw new OperationFailException("error.social.gatewayEndpointNotConfigured");
        }
        return StrUtil.removeSuffix(gatewayBase, "/");
    }

    /// 解析跳转型平台 OAuth 回调的透传目标地址(网关 H5 透传页专用)
    ///
    /// 跳转型平台回调统一落在支付网关域名的透传路径, 透传页凭 client+mode 查询本方法,
    /// 跳回对应端的原有回调页(各端页面与 exchange 接口零改动). 仅对跳转型平台开放,
    /// 目标域名取自端点配置, 天然构成跳转白名单.
    public String resolveTransitTarget(String source, String client, String mode) {
        SocialSourceEnum socialSource = SocialSourceEnum.of(source);
        if (Objects.isNull(socialSource) || !socialSource.isPlatformRedirect()) {
            // 社交登录: 透传目标仅对跳转型平台(支付宝/公众号)开放
            throw new OperationFailException("error.social.unsupportedSource");
        }
        SocialClientEnum socialClient = this.requireSocialClient(client);
        // 透传页路径已区分登录/绑定, mode 非法值按登录兜底
        SocialAuthMode authMode = StrUtil.equalsIgnoreCase(mode, SocialAuthMode.BIND.name())
            ? SocialAuthMode.BIND
            : SocialAuthMode.LOGIN;
        String baseUrl = this.requireBaseUrl(socialClient);
        String callbackPath = authMode == SocialAuthMode.BIND
            ? BIND_CALLBACK_PATH
            : LOGIN_CALLBACK_PATH;
        return StrUtil.removeSuffix(baseUrl, "/") + callbackPath + "/" + socialSource.getCode();
    }

    /// 加载已启用的平台配置(不存在则抛业务异常)
    private SocialLoginConfig loadEnabledConfig(String source) {
        SocialLoginConfig config = socialLoginConfigService.findEnabledBySource(source);
        if (Objects.isNull(config)) {
            // 社交登录: 平台未配置或未启用
            throw new OperationFailException("error.social.configNotExist");
        }
        return config;
    }
}
