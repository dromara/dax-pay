package cn.daxpay.open.payment.auth.platform;

import cn.daxpay.open.payment.unipay.param.assist.AuthCodeParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.daxpay.open.payment.wx.facade.WxAppFacade;
import cn.daxpay.open.payment.wx.facade.WxAppView;
import cn.daxpay.open.platform.capability.wechat.auth.result.WechatAuthResult;
import cn.daxpay.open.platform.capability.wechat.auth.result.WechatAuthUrlResult;
import cn.daxpay.open.platform.capability.wechat.auth.service.WechatMpAuthService;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.unipay.ChannelAuthStatusEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
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
/// 通过 [WxAppFacade] 从微信主数据(wx_platform_app)解析公众号(OFFICIAL_ACCOUNT)类型的平台应用。
@Slf4j
@Component
@RequiredArgsConstructor
public class WechatMpAuthProvider implements PlatformAuthProvider {

    private final AuthSessionStore authSessionStore;
    private final WxAppFacade wxAppFacade;
    private final PlatformUrlConfigService platformUrlConfigService;
    private final WechatMpAuthService wechatMpAuthService;

    @Override
    public String sourceCode() {
        return AuthSession.SOURCE_PLATFORM_MP;
    }

    @Override
    public AuthUrlResult generateAuthUrl(String returnPath) {
        WxAppView app = loadPlatformAppOrThrow();
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
        WechatAuthUrlResult result = wechatMpAuthService.generateAuthUrl(redirectUri, app.wxAppId(), app.appSecret(), authToken);
        authSessionStore.saveWaitingResult(queryCode);
        return new AuthUrlResult().setAuthUrl(result.getAuthUrl()).setQueryCode(queryCode).setAuthToken(authToken);
    }

    @Override
    public AuthResult auth(AuthCodeParam param, AuthSession session) {
        WxAppView app = loadPlatformAppOrThrow();
        WechatAuthResult data = wechatMpAuthService.getTokenAndOpenId(param.getAuthCode(), app.wxAppId(), app.appSecret());
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

    /// 加载平台级公众号应用(从微信主数据)
    ///
    /// 通过 JSAPI 能力推导 OFFICIAL_ACCOUNT 类型, 从 wx_platform_app 主数据查找唯一平台应用。
    /// 找不到应用时 facade 会抛 appNotConfigured; appSecret 未配置时本方法补充检查。
    private WxAppView loadPlatformAppOrThrow() {
        WxAppView app = wxAppFacade.resolve(null, null,
                PayCapabilityEnum.WECHAT_JSAPI.getCode(), null,
                ProductEnum.WECHAT_PAY.getCode());
        if (StrUtil.isBlank(app.appSecret())) {
            // 微信: 平台级公众号应用授权密钥未配置
            throw new BizInfoException(CommonErrorCode.SYSTEM_ERROR,
                    "error.payment.wx.appNotConfigured", "official_account");
        }
        return app;
    }
}
