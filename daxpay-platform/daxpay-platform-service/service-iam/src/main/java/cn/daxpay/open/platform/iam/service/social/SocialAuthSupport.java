package cn.daxpay.open.platform.iam.service.social;

import java.util.List;
import java.util.Objects;

import cn.daxpay.open.platform.capability.auth.authentication.UserInfoStatusCheck;
import cn.daxpay.open.platform.capability.auth.entity.AuthInfoResult;
import cn.daxpay.open.platform.capability.auth.entity.LoginAuthContext;
import cn.daxpay.open.platform.capability.auth.exception.LoginFailureException;
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
import cn.daxpay.open.platform.iam.result.user.UserInfoResult;
import cn.daxpay.open.platform.iam.service.social.other.AlipaySocialAuthRequestFactory;
import cn.daxpay.open.platform.iam.service.user.UserQueryService;
import cn.daxpay.open.platform.system.entity.config.platform.auth.PlatformWechatMpAuthConfig;
import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformUrlConfig;
import cn.daxpay.open.platform.system.service.config.auth.PlatformWechatMpAuthConfigService;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/// # 社交授权共享支撑
///
/// 收编标准 OAuth 编排([SocialLoginService])与跨设备扫码族
/// (social.scan 子包的 SocialBindScanService/SocialLoginScanService)共用的授权链路原语:
/// code 兑换(doExchange)、扫码授权地址构建、公众号平台级凭据回填、回调地址拼装、
/// 登录用户校验链. 本类无业务状态, 由各编排 service 组合注入使用.
///
@Component
@RequiredArgsConstructor
public class SocialAuthSupport {

    /// 登录回调路径
    public static final String LOGIN_CALLBACK_PATH = "/auth/oauth-callback";

    /// 绑定回调路径
    public static final String BIND_CALLBACK_PATH = "/auth/social-bind-callback";

    private final SocialAuthRequestFactory socialAuthRequestFactory;

    private final AlipaySocialAuthRequestFactory alipaySocialAuthRequestFactory;

    private final SocialLoginConfigService socialLoginConfigService;

    private final PlatformUrlConfigService platformUrlConfigService;

    private final PlatformWechatMpAuthConfigService platformWechatMpAuthConfigService;

    private final UserQueryService userQueryService;

    private final PlatformStarterProperties platformStarterProperties;

    private final List<UserInfoStatusCheck> userInfoStatusChecks;

    /// 共享: code 换 AuthUser(登录/绑定共用), redirectUri 拼装内聚于此保证与发起侧一致
    public AuthUser doExchange(String code, String state, String source,
                               SocialClientEnum socialClient, SocialAuthMode mode) {
        return this.doExchange(code, state, source, socialClient, mode, null);
    }

    /// @param redirectUriOverride 非空时覆盖默认回调地址拼装(扫码绑定走网关独立回调路径)
    public AuthUser doExchange(String code, String state, String source,
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

    /// 构建扫码授权地址(回调走网关域名独立路径, 绕开透传页; 绑定/登录按各自回调路径)
    public String buildScanAuthorizeUrl(SocialSourceEnum socialSource, String clientCode, String state, String redirectPath) {
        SocialLoginConfig config = this.loadEnabledConfig(socialSource.getCode());
        String redirectUri = this.requireGatewayBase() + redirectPath;
        SocialAuthConfig authConfig = socialLoginConfigService.buildAuthConfig(config, redirectUri);
        // 公众号: 凭据来自平台级配置
        this.fillPlatformRedirectCredentials(socialSource, authConfig);
        SocialAuthRequest request = this.createAuthRequest(socialSource, authConfig);
        return request.authorize(state);
    }

    /// 社交登录用户校验: client 归属 + 状态检查链
    public void validateSocialLoginUser(Long userId, String clientCode, String source,
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

    /// 平台级跳转型凭据回填(公众号 AppId/Secret 等)
    public void fillPlatformRedirectCredentials(SocialSourceEnum socialSource, SocialAuthConfig authConfig) {
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
    public SocialAuthRequest createAuthRequest(SocialSourceEnum socialSource, SocialAuthConfig authConfig) {
        if (socialSource == SocialSourceEnum.ALIPAY) {
            return alipaySocialAuthRequestFactory.create(authConfig);
        }
        return socialAuthRequestFactory.create(socialSource, authConfig);
    }

    /// 构建回调基础地址(不含 source 后缀, JustAuth 层再拼 /{source})
    ///
    /// 跳转型平台(支付宝/公众号): 回调走支付网关域名的透传路径, 路径带 client 段供
    /// H5 透传页识别目标端, 解决同一 AppId 下登录回调与支付认证回调竞争开放平台回调名额的问题.
    /// 标准型平台: 沿用各端自身域名的回调路径.
    public String buildRedirectUri(String baseUrl, SocialSourceEnum socialSource,
                                   SocialClientEnum socialClient, SocialAuthMode mode) {
        String callbackPath = mode == SocialAuthMode.BIND
            ? BIND_CALLBACK_PATH
            : LOGIN_CALLBACK_PATH;
        if (socialSource.isPlatformRedirect()) {
            return this.requireGatewayBase() + callbackPath + "/" + socialClient.getCode();
        }
        return StrUtil.removeSuffix(baseUrl, "/") + callbackPath;
    }

    /// 校验并解析社交登录终端(仅 admin/merchant)
    public SocialClientEnum requireSocialClient(String clientCode) {
        return SocialClientEnum.findByCode(clientCode)
                .orElseThrow(() -> {
                    // 社交登录: 不支持的身份域编码(仅 admin/merchant)
                    throw new OperationFailException("error.social.unsupportedClient");
                });
    }

    /// 解析并校验 baseUrl 非空
    public String requireBaseUrl(SocialClientEnum socialClient) {
        String baseUrl = this.resolveBaseUrl(socialClient);
        if (StrUtil.isBlank(baseUrl)) {
            throw new OperationFailException(this.endpointMissingKey(socialClient));
        }
        return baseUrl;
    }

    /// 按 client 解析前端 baseUrl
    public String resolveBaseUrl(SocialClientEnum socialClient) {
        PlatformUrlConfig urlConfig = platformUrlConfigService.getUrlConfig();
        return socialClient.resolveBaseUrl(urlConfig);
    }

    /// 端点缺失时的 i18n key(区分运营/商户便于配置)
    public String endpointMissingKey(SocialClientEnum socialClient) {
        return socialClient == SocialClientEnum.MERCHANT
                ? "error.social.merchantEndpointNotConfigured"
                : "error.social.adminEndpointNotConfigured";
    }

    /// 校验并返回支付网关前端地址(去尾斜杠), 未配置时抛业务异常
    public String requireGatewayBase() {
        String gatewayBase = platformUrlConfigService.getUrlConfig().getPaymentGatewayBaseUrl();
        if (StrUtil.isBlank(gatewayBase)) {
            // 社交登录: 跳转型平台回调依赖支付网关域名, 未配置时无法生成授权地址
            throw new OperationFailException("error.social.gatewayEndpointNotConfigured");
        }
        return StrUtil.removeSuffix(gatewayBase, "/");
    }

    /// 加载已启用的平台配置(不存在则抛业务异常)
    public SocialLoginConfig loadEnabledConfig(String source) {
        SocialLoginConfig config = socialLoginConfigService.findEnabledBySource(source);
        if (Objects.isNull(config)) {
            // 社交登录: 平台未配置或未启用
            throw new OperationFailException("error.social.configNotExist");
        }
        return config;
    }
}
