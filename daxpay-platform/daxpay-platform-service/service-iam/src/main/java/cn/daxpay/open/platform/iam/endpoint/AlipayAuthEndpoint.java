package cn.daxpay.open.platform.iam.endpoint;

import cn.daxpay.open.platform.capability.alipay.auth.config.AlipayAuthConfig;
import cn.daxpay.open.platform.capability.alipay.auth.result.AlipayAuthResult;
import cn.daxpay.open.platform.capability.alipay.auth.service.AlipayAuthCapability;
import cn.daxpay.open.platform.capability.auth.util.SecurityUtil;
import cn.daxpay.open.platform.capability.social.justauth.SocialSourceEnum;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthUser;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.iam.enums.SocialAuthMode;
import cn.daxpay.open.platform.iam.enums.SocialClientEnum;
import cn.daxpay.open.platform.iam.result.social.SocialExchangeResult;
import cn.daxpay.open.platform.iam.entity.social.SocialLoginConfig;
import cn.daxpay.open.platform.iam.service.social.IamSocialLoginHandler;
import cn.daxpay.open.platform.iam.service.social.IamUserSocialBindStore;
import cn.daxpay.open.platform.iam.service.social.SocialLoginConfigService;
import cn.daxpay.open.platform.system.entity.config.platform.PlatformUrlConfig;
import cn.daxpay.open.platform.system.service.config.PlatformAlipayAuthConfigService;
import cn.daxpay.open.platform.system.service.config.PlatformUrlConfigService;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/// # 支付宝授权登录端点
///
/// 支付宝非标准 OAuth2(走 `alipay.system.oauth.token`), 不复用 JustAuth 的 [SocialEndpoint],
/// 独立实现 render/exchange。但登录态签发、绑定关系存储复用 iam 现有体系
/// ([IamSocialLoginHandler] / [IamUserSocialBindStore]), 绑定写入 `iam_user_social` 表(source=`alipay`)。
///
/// ## 流程
/// 1. 前端调 `render` 获取授权链接(用平台级支付宝配置拼 `publicAppAuthorize.htm`)
/// 2. 前端 `location.href` 跳转, 用户在支付宝授权
/// 3. 支付宝回调到前端 `/auth/oauth-callback/alipay?auth_code=xxx&state=xxx`
/// 4. 前端调 `exchange` 用 auth_code 换 userId, 完成登录或绑定
///
@Slf4j
@IgnoreAuth
@Tag(name = "支付宝授权登录")
@RestController
@RequestMapping("/social/alipay")
@RequiredArgsConstructor
public class AlipayAuthEndpoint {

    /// 支付宝在三方登录体系中的 source 编码
    private static final String SOURCE = SocialSourceEnum.ALIPAY.getCode();

    /// 授权范围: auth_user(需用户确认, 可获取用户信息)
    private static final String SCOPE = "auth_user";

    /// 登录回调路径(前端路由, 与标准 OAuth2 平台同构: /auth/oauth-callback/{source})
    private static final String LOGIN_CALLBACK_PATH = "/auth/oauth-callback/" + SOURCE;

    /// 绑定回调路径(前端路由: /auth/social-bind-callback/{source})
    private static final String BIND_CALLBACK_PATH = "/auth/social-bind-callback/" + SOURCE;

    private final PlatformAlipayAuthConfigService platformAlipayAuthConfigService;
    private final AlipayAuthCapability alipayAuthCapability;
    private final PlatformUrlConfigService platformUrlConfigService;
    private final SocialLoginConfigService socialLoginConfigService;
    private final IamUserSocialBindStore socialBindStore;
    private final IamSocialLoginHandler socialLoginHandler;

    /// 生成支付宝授权地址
    /// @param client 终端编码(admin/merchant), 用于解析端点配置中的 baseUrl
    /// @param mode   授权场景(不传则按登录态判断: 已登录=绑定, 未登录=登录)
    @GetMapping("/render")
    public Result<String> render(@RequestParam String client,
                                 @RequestParam(required = false) String mode) {
        // 0. 校验登录配置已启用(与标准 OAuth 平台一致)
        this.requireLoginEnabled();
        // 1. 读平台级配置
        AlipayAuthConfig capabilityConfig = platformAlipayAuthConfigService.toCapabilityConfig();
        if (!alipayAuthCapability.isConfigured(capabilityConfig)) {
            // 支付宝: 平台级支付宝配置不完整, 请先在「三方平台管理」中配置
            throw new OperationFailException("error.social.alipayNotConfigured");
        }
        // 2. 按 client 解析前端 baseUrl
        PlatformUrlConfig urlConfig = platformUrlConfigService.getUrlConfig();
        String baseUrl = SocialClientEnum.of(client).resolveBaseUrl(urlConfig);
        if (StrUtil.isBlank(baseUrl)) {
            // 社交登录: 端点配置缺失
            throw new OperationFailException("error.social.endpointNotConfigured");
        }
        // 3. 拼回调地址(LOGIN/BIND 不同路径)
        SocialAuthMode authMode = this.resolveMode(mode);
        String callbackPath = authMode == SocialAuthMode.BIND ? BIND_CALLBACK_PATH : LOGIN_CALLBACK_PATH;
        String redirectUri = baseUrl + callbackPath;
        // 4. 生成 state(OAuth2 合规, CSRF 防护)
        String state = IdUtil.fastSimpleUUID();
        // 5. 拼授权链接(平台级配置固定生产环境, 不再读取 sandbox)
        String authUrl = alipayAuthCapability.generateAuthUrl(capabilityConfig, redirectUri, SCOPE, state, false);
        return Res.ok(authUrl);
    }

    /// 支付宝授权码兑换(LOGIN/BIND 由 mode 决定, 未传按登录态判断)
    /// @param authCode 支付宝回调回传的 auth_code
    /// @param state    render 阶段生成的 state(原样回传, 目前仅做日志, 不做强校验)
    /// @param client   终端编码
    /// @param mode     授权场景
    @PostMapping("/exchange")
    public Result<SocialExchangeResult> exchange(@RequestParam("authCode") String authCode,
                                                  @RequestParam("state") String state,
                                                  @RequestParam("client") String client,
                                                  @RequestParam(name = "mode", required = false) String mode,
                                                  HttpServletRequest request,
                                                  HttpServletResponse response) {
        try {
            // 0. 校验登录配置已启用
            this.requireLoginEnabled();
            // 1. 换 userId
            AlipayAuthConfig capabilityConfig = platformAlipayAuthConfigService.toCapabilityConfig();
            AlipayAuthResult authResult = alipayAuthCapability.getUserId(capabilityConfig, authCode);
            String alipayUserId = authResult.getUserId();
            if (StrUtil.isBlank(alipayUserId)) {
                return Res.ok(new SocialExchangeResult().setError("oauth_failed"));
            }
            // 2. 构造 AuthUser(复用现有绑定存储, source=alipay, uuid=支付宝userId)
            AuthUser authUser = new AuthUser()
                    .setSource(SOURCE)
                    .setUuid(alipayUserId)
                    .setNickname("支付宝用户");
            // 3. 按场景分流
            SocialAuthMode authMode = this.resolveMode(mode);
            if (authMode == SocialAuthMode.BIND) {
                // 绑定场景: 必须已登录
                Long userId = SecurityUtil.getUserId();
                socialBindStore.saveBind(userId, client, authUser);
                return Res.ok(new SocialExchangeResult().setResult("bind_success"));
            }
            // 登录场景: 查绑定关系
            Long userId = socialBindStore.findUserIdBySourceAndOpenId(SOURCE, alipayUserId).orElse(null);
            if (userId == null) {
                // 未绑定
                return Res.ok(new SocialExchangeResult().setError("unbind"));
            }
            String token = socialLoginHandler.login(userId, client, SOURCE, request, response);
            return Res.ok(new SocialExchangeResult().setToken(token));
        } catch (Exception e) {
            log.error("支付宝授权登录兑换失败: authCode={}, msg={}", authCode, e.getMessage(), e);
            return Res.ok(new SocialExchangeResult().setError("oauth_failed"));
        }
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

    /// 校验支付宝登录已在「三方平台登录配置」中启用
    private void requireLoginEnabled() {
        SocialLoginConfig config = socialLoginConfigService.findEnabledBySource(SOURCE);
        if (config == null) {
            // 社交登录: 平台配置不存在或未启用
            throw new OperationFailException("error.social.configNotExist");
        }
    }
}
