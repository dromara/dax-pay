package cn.daxpay.open.payment.auth.platform;

import cn.daxpay.open.payment.unipay.param.assist.AuthCodeParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.daxpay.open.platform.capability.wechat.auth.result.WechatAuthResult;
import cn.daxpay.open.platform.capability.wechat.auth.result.WechatAuthUrlResult;
import cn.daxpay.open.platform.capability.wechat.auth.service.WechatMpAuthService;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.unipay.ChannelAuthStatusEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.system.entity.config.platform.auth.PlatformWechatMpAuthConfig;
import cn.daxpay.open.platform.system.service.config.auth.PlatformWechatMpAuthConfigService;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import cn.daxpay.open.payment.auth.core.AuthScene;
import cn.daxpay.open.payment.auth.core.AuthSession;
import cn.daxpay.open.payment.auth.core.AuthSessionStore;
import cn.daxpay.open.payment.auth.core.AuthRedirectUri;

/// # 微信公众号平台级认证 Provider
///
/// 会话标记 `source=platform_mp`, 仅调试场景使用(网关聚合/收银台/码牌支付走通道应用策略)。
///
/// ## 配置来源
/// 从「三方平台管理」的微信公众号认证配置([PlatformWechatMpAuthConfigService])读取 appId/appSecret,
/// 与 iam 社交登录共用同一套配置。认证调试的「微信公众号」Tab 调试的是登录(OAuth 网页授权取 openId),
/// 不是支付, 因此不应依赖支付应用(wx_platform_app)的数据。
@Slf4j
@Component
@RequiredArgsConstructor
public class WechatMpAuthProvider implements PlatformAuthProvider {

    private final AuthSessionStore authSessionStore;
    private final PlatformWechatMpAuthConfigService platformWechatMpAuthConfigService;
    private final PlatformUrlConfigService platformUrlConfigService;
    private final WechatMpAuthService wechatMpAuthService;

    @Override
    public String sourceCode() {
        return AuthSession.SOURCE_PLATFORM_MP;
    }

    @Override
    public AuthUrlResult generateAuthUrl(String returnPath) {
        var config = loadMpAuthConfigOrThrow();
        String authToken = IdUtil.fastSimpleUUID();
        String queryCode = RandomUtil.randomString(10);
        AuthSession session = new AuthSession()
                .setSource(AuthSession.SOURCE_PLATFORM_MP)
                .setQueryCode(queryCode)
                .setReturnPath(returnPath)
                .setScene(AuthScene.PLATFORM.getCode());
        authSessionStore.saveSession(authToken, session);
        // redirect_uri 为固定路径(见 [AuthRedirectUri]), authToken 通过 OAuth state 透传
        String redirectUri = AuthRedirectUri.WECHAT.buildRedirectUri(platformUrlConfigService);
        WechatAuthUrlResult result = wechatMpAuthService.generateAuthUrl(redirectUri, config.getAppId(), config.getAppSecret(), authToken);
        authSessionStore.saveWaitingResult(queryCode);
        return new AuthUrlResult().setAuthUrl(result.getAuthUrl()).setQueryCode(queryCode).setAuthToken(authToken);
    }

    @Override
    public AuthResult auth(AuthCodeParam param, AuthSession session) {
        PlatformWechatMpAuthConfig config = loadMpAuthConfigOrThrow();
        WechatAuthResult data = wechatMpAuthService.getTokenAndOpenId(param.getAuthCode(), config.getAppId(), config.getAppSecret());
        if (StrUtil.isBlank(data.getOpenId())) {
            // 微信: 获取openId失败
            throw new BizInfoException(CommonErrorCode.SYSTEM_ERROR, "error.channel.wechat.authFailed", "openId is blank");
        }
        AuthResult authResult = new AuthResult()
                .setOpenId(data.getOpenId())
                .setAccessToken(data.getAccessToken())
                .setStatus(ChannelAuthStatusEnum.SUCCESS.getCode());
        fillReturnPath(authResult, session);
        authSessionStore.writeResultByQueryCode(param.getQueryCode(), session, authResult);
        return authResult;
    }

    /// 加载微信公众号认证配置(从三方平台管理)
    ///
    /// 与 iam 社交登录共用同一套配置(appId/appSecret), 用于 OAuth 网页授权获取 openId。
    /// 配置缺失时抛 mpAuthNotConfigured, 文案指向「三方平台管理 → 微信公众号认证配置」。
    private PlatformWechatMpAuthConfig loadMpAuthConfigOrThrow() {
        PlatformWechatMpAuthConfig config = platformWechatMpAuthConfigService.getWechatMpAuthConfig();
        if (StrUtil.isBlank(config.getAppId()) || StrUtil.isBlank(config.getAppSecret())) {
            // 微信: 三方平台管理中微信公众号认证配置未填写
            throw new BizInfoException(CommonErrorCode.SYSTEM_ERROR,
                    "error.payment.wx.mpAuthNotConfigured");
        }
        return config;
    }
}