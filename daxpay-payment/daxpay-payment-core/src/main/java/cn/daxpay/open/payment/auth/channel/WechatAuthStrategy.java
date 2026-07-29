package cn.daxpay.open.payment.auth.channel;

import cn.daxpay.open.payment.auth.core.AppScopeEnum;
import cn.daxpay.open.payment.auth.core.AuthRedirectUri;
import cn.daxpay.open.payment.auth.core.AuthSession;
import cn.daxpay.open.payment.unipay.param.assist.AuthCodeParam;
import cn.daxpay.open.payment.unipay.param.assist.GenerateAuthUrlParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.daxpay.open.payment.wx.facade.WxAppFacade;
import cn.daxpay.open.payment.wx.facade.WxAppView;
import cn.daxpay.open.platform.capability.wechat.auth.result.WechatAuthResult;
import cn.daxpay.open.platform.capability.wechat.auth.result.WechatAuthUrlResult;
import cn.daxpay.open.platform.capability.wechat.auth.service.WechatMpAuthService;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.unipay.ChannelAuthTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 微信认证策略
///
/// 微信公众号 OAuth 取 openId。应用凭证(wxAppId + appSecret)由策略内部据
/// appScope(档位) + appRefId(主键)调 [WxAppFacade#getById] 查得。
/// 入口层已按"先查通道、通道不存在查平台"原则 resolve 选定应用, 此处只做凭证加载。
///
/// generateAuthUrl 时把 appScope/appRefId 写入 session, 供回调 doAuth 恢复凭证。
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatAuthStrategy implements ChannelAuthStrategy {

    private final WechatMpAuthService wechatMpAuthService;
    private final PlatformUrlConfigService platformUrlConfigService;
    private final WxAppFacade wxAppFacade;

    @Override
    public ChannelAuthTypeEnum getAuthType() {
        return ChannelAuthTypeEnum.WECHAT;
    }

    /// 生成公众号 OAuth 授权链接
    ///
    /// 据 param 的档位标识加载应用凭证, 拼接固定回调地址({paymentGatewayBaseUrl}/auth/wechat),
    /// 委托 capability-wechat 生成微信 OAuth URL。会话标识 authToken 通过 OAuth state 参数透传。
    /// 同时把 appScope/appRefId 写入 session, 供回调恢复凭证。
    @Override
    public AuthUrlResult generateAuthUrl(GenerateAuthUrlParam param, String authToken, AuthSession session) {
        WxAppView app = loadApp(param.getAppScope(), param.getAppRefId());
        // 写入应用引用供回调 doAuth 恢复凭证
        session.setAppScope(app.scope().getCode());
        session.setAppRefId(app.id());
        String redirectUri = AuthRedirectUri.WECHAT.buildRedirectUri(platformUrlConfigService);
        WechatAuthUrlResult result = wechatMpAuthService.generateAuthUrl(
                redirectUri, app.wxAppId(), app.appSecret(), authToken);
        return new AuthUrlResult().setAuthUrl(result.getAuthUrl());
    }

    /// 通过授权 code(公众号 OAuth 回调的 code)换取 openId
    ///
    /// 据 session 的档位标识加载应用凭证, 用 code 换 openId。
    @Override
    public AuthResult doAuth(AuthCodeParam param, AuthSession session) {
        WxAppView app = loadApp(session.getAppScope(), session.getAppRefId());
        WechatAuthResult data = wechatMpAuthService.getTokenAndOpenId(
                param.getAuthCode(), app.wxAppId(), app.appSecret());
        if (StrUtil.isBlank(data.getOpenId())) {
            // 微信: 获取openId失败
            throw new BizInfoException(CommonErrorCode.SYSTEM_ERROR, "error.channel.wechat.authFailed", "openId is blank");
        }
        return new AuthResult()
                .setOpenId(data.getOpenId())
                .setAccessToken(data.getAccessToken());
    }

    /// 按档位 + 主键加载微信应用凭证(含解密后的 appSecret)
    private WxAppView loadApp(String appScope, Long appRefId) {
        if (StrUtil.isBlank(appScope) || appRefId == null) {
            // 微信: 认证应用引用缺失(appScope/appRefId 未传入)
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.payment.wx.appNotConfigured", "auth");
        }
        AppScopeEnum scope = AppScopeEnum.findByCode(appScope);
        return wxAppFacade.getById(scope, appRefId);
    }
}
