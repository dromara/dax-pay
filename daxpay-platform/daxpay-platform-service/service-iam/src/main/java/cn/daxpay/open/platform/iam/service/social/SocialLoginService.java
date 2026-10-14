package cn.daxpay.open.platform.iam.service.social;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.daxpay.open.platform.capability.auth.exception.LoginFailureException;
import cn.daxpay.open.platform.capability.auth.util.SecurityUtil;
import cn.daxpay.open.platform.capability.social.justauth.SocialAuthConfig;
import cn.daxpay.open.platform.capability.social.justauth.SocialSourceEnum;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthUser;
import cn.daxpay.open.platform.capability.social.justauth.request.SocialAuthRequest;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.iam.entity.social.SocialLoginConfig;
import cn.daxpay.open.platform.iam.enums.SocialAuthMode;
import cn.daxpay.open.platform.iam.enums.SocialClientEnum;
import cn.daxpay.open.platform.iam.result.social.SocialBindResult;
import cn.daxpay.open.platform.iam.result.social.SocialCallbackUrlResult;
import cn.daxpay.open.platform.iam.result.social.SocialEnabledPlatformResult;
import cn.daxpay.open.platform.iam.result.social.SocialExchangeResult;
import cn.daxpay.open.platform.iam.result.user.UserInfoResult;
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

/// # 第三方社交登录编排服务(标准 OAuth)
///
/// 编排 OAuth2 授权流程: 授权地址生成(render)、授权码兑换(login/bind)、
/// 绑定关系查询与解绑. 采用前端回调模式: 第三方平台直接重定向到前端回调页,
/// 前端拿到 code+state 后调用 exchange 接口完成换 token.
/// state 仅用于 OAuth2 合规(CSRF), 不携带业务上下文.
/// 登录和绑定的区分由回调页路由决定(两个不同的前端页面), exchange 时显式传 source + client.
/// 授权链路共享原语(code 兑换/凭据回填/回调拼装)收口在 [SocialAuthSupport];
/// 公众号跨设备扫码绑定/扫码登录见 social.scan 子包的 SocialBindScanService/SocialLoginScanService.
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

    private final SocialAuthSupport socialAuthSupport;

    private final IamUserSocialBindStore socialBindStore;

    private final IamSocialLoginHandler socialLoginHandler;

    private final SocialLoginConfigService socialLoginConfigService;

    private final PlatformUrlConfigService platformUrlConfigService;

    private final UserQueryService userQueryService;

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
        String path = mode == SocialAuthMode.BIND
            ? SocialAuthSupport.BIND_CALLBACK_PATH
            : SocialAuthSupport.LOGIN_CALLBACK_PATH;
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
        SocialClientEnum socialClient = socialAuthSupport.requireSocialClient(client);
        // 加载平台配置(全局唯一)
        SocialAuthConfig authConfig = this.buildAuthConfig(source, socialClient, mode);
        // 应用内自动登录: 企微走网页授权、钉钉省略 prompt=consent(其余平台沿用默认授权地址)
        if (Boolean.TRUE.equals(silent)) {
            authConfig.setSilent(true);
        }
        // state 仅用于 OAuth2 合规, 不缓存业务上下文
        String state = IdUtil.fastSimpleUUID();
        return socialAuthSupport.createAuthRequest(SocialSourceEnum.of(source), authConfig).authorize(state);
    }

    /// 按场景组装授权配置(redirectUri 拼装 + 平台级凭据回填)
    private SocialAuthConfig buildAuthConfig(String source, SocialClientEnum socialClient, String mode) {
        SocialLoginConfig config = socialAuthSupport.loadEnabledConfig(source);
        SocialSourceEnum socialSource = SocialSourceEnum.of(source);
        if (Objects.isNull(socialSource)) {
            // 社交登录: 不支持的平台
            throw new OperationFailException("error.social.unsupportedSource");
        }
        // 按 client 解析前端 baseUrl
        String baseUrl = socialAuthSupport.resolveBaseUrl(socialClient);
        if (StrUtil.isBlank(baseUrl)) {
            // 社交登录: 运营/商户端前端地址未配置
            throw new OperationFailException(socialAuthSupport.endpointMissingKey(socialClient));
        }
        SocialAuthMode authMode = this.resolveMode(mode);
        // 根据场景拼接回调基础地址(跳转型平台走支付网关域名的透传路径)
        String redirectUri = socialAuthSupport.buildRedirectUri(baseUrl, socialSource, socialClient, authMode);
        SocialAuthConfig authConfig = socialLoginConfigService.buildAuthConfig(config, redirectUri);
        // 公众号: 凭据来自平台级配置, 不写在 iam_social_login_config
        socialAuthSupport.fillPlatformRedirectCredentials(socialSource, authConfig);
        return authConfig;
    }

    /// OAuth 授权码兑换 - 登录场景(公开, 无需认证)
    /// 前端登录回调页收到第三方平台的 code+state 后调用此方法,
    /// 后端完成 code 换 token, 查绑定关系, 签发登录令牌.
    public SocialExchangeResult exchangeForLogin(String code, String state,
                                                  String source, String clientCode,
                                                  HttpServletRequest request, HttpServletResponse response) {
        try {
            SocialClientEnum socialClient = socialAuthSupport.requireSocialClient(clientCode);
            AuthUser authUser = socialAuthSupport.doExchange(code, state, source, socialClient, SocialAuthMode.LOGIN);
            // 查绑定关系
            Long userId = socialBindStore.findUserIdBySourceAndOpenId(source, authUser.getUuid()).orElse(null);
            if (Objects.isNull(userId)) {
                // 未绑定
                return new SocialExchangeResult().setError("unbind");
            }
            // 身份域 + 用户状态检查(对齐密码路径 UserInfoStatusCheck 链)
            socialAuthSupport.validateSocialLoginUser(userId, clientCode, source, request, response);
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

    /// OAuth 授权码兑换 - 绑定场景(需登录)
    /// 前端绑定回调页收到第三方平台的 code+state 后调用此方法,
    /// 后端完成 code 换 token, 保存绑定关系到当前登录用户.
    public SocialExchangeResult exchangeForBind(String code, String state,
                                                 String source, String clientCode) {
        try {
            SocialClientEnum socialClient = socialAuthSupport.requireSocialClient(clientCode);
            // 绑定场景必须已登录
            Long userId = SecurityUtil.getUserId();
            // 当前用户必须属于该身份域, 防止跨端绑定
            UserInfoResult userInfo = userQueryService.findById(userId);
            if (!Objects.equals(clientCode, userInfo.getClientCode())) {
                // 社交登录: 绑定用户不属于当前终端
                throw new OperationFailException("error.social.bind.clientMismatch");
            }
            AuthUser authUser = socialAuthSupport.doExchange(code, state, source, socialClient, SocialAuthMode.BIND);
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
        SocialClientEnum socialClient = socialAuthSupport.requireSocialClient(client);
        // 透传页路径已区分登录/绑定, mode 非法值按登录兜底
        SocialAuthMode authMode = StrUtil.equalsIgnoreCase(mode, SocialAuthMode.BIND.name())
            ? SocialAuthMode.BIND
            : SocialAuthMode.LOGIN;
        String baseUrl = socialAuthSupport.requireBaseUrl(socialClient);
        String callbackPath = authMode == SocialAuthMode.BIND
            ? SocialAuthSupport.BIND_CALLBACK_PATH
            : SocialAuthSupport.LOGIN_CALLBACK_PATH;
        return StrUtil.removeSuffix(baseUrl, "/") + callbackPath + "/" + socialSource.getCode();
    }
}
