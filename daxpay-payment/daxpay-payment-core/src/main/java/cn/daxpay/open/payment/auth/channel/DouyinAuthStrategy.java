package cn.daxpay.open.payment.auth.channel;

import cn.daxpay.open.payment.auth.core.AuthRedirectUri;
import cn.daxpay.open.payment.auth.core.AuthSession;
import cn.daxpay.open.payment.douyin.facade.DouyinAppFacade;
import cn.daxpay.open.payment.douyin.facade.DyAppView;
import cn.daxpay.open.payment.douyin.enums.DyAppScopeEnum;
import cn.daxpay.open.payment.unipay.param.assist.AuthCodeParam;
import cn.daxpay.open.payment.unipay.param.assist.GenerateAuthUrlParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.daxpay.open.platform.capability.douyin.auth.result.DouyinAuthResult;
import cn.daxpay.open.platform.capability.douyin.auth.service.DouyinH5AuthService;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.unipay.ChannelAuthTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 抖音认证策略
///
/// 抖音 H5 silent_auth 取 openId。应用凭证(douyinAppId + appSecret)由策略内部据
/// dyAppScope(档位) + dyAppRefId(主键)调 [DouyinAppFacade#getById] 查得。
/// 入口层已选定网站应用(web_app), 此处只做凭证加载。
///
/// generateAuthUrl 时把 dyAppScope/dyAppRefId 写入 session, 供回调 doAuth 恢复凭证。
///
/// H5 silent_auth 固定使用网站应用(web_app), 与支付能力(按 capability 解析的小程序/移动应用)不同。
@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinAuthStrategy implements ChannelAuthStrategy {

    private final DouyinH5AuthService douyinH5AuthService;
    private final DouyinAppFacade douyinAppFacade;
    private final PlatformUrlConfigService platformUrlConfigService;

    @Override
    public ChannelAuthTypeEnum getAuthType() {
        return ChannelAuthTypeEnum.DOUYIN;
    }

    /// 生成抖音 H5 静默授权链接
    ///
    /// 据 param 的档位标识加载网站应用凭证, 拼接回调地址, 委托 capability-douyin 生成授权 URL。
    /// 会话标识 authToken 通过 OAuth state 参数透传。
    /// 同时把 dyAppScope/dyAppRefId 写入 session, 供回调 doAuth 恢复凭证。
    @Override
    public AuthUrlResult generateAuthUrl(GenerateAuthUrlParam param, String authToken, AuthSession session) {
        DyAppView app = loadApp(param.getDyAppScope(), param.getDyAppRefId());
        // 写入应用引用供回调 doAuth 恢复凭证
        session.setDyAppScope(app.scope().getCode());
        session.setDyAppRefId(app.id());
        String redirectUri = AuthRedirectUri.DOUYIN.buildRedirectUri(platformUrlConfigService);
        String authUrl = douyinH5AuthService.buildSilentAuthUrl(
                app.douyinAppId(), redirectUri, authToken);
        return new AuthUrlResult().setAuthUrl(authUrl);
    }

    /// 通过授权 code 换取 openId
    ///
    /// 据 session 的档位标识加载网站应用凭证, 用 code 换 openId。
    @Override
    public AuthResult doAuth(AuthCodeParam param, AuthSession session) {
        DyAppView app = loadApp(session.getDyAppScope(), session.getDyAppRefId());
        DouyinAuthResult data = douyinH5AuthService.getOpenIdByCode(
                app.douyinAppId(), app.appSecret(), param.getAuthCode());
        if (StrUtil.isBlank(data.getOpenId())) {
            // 抖音: 获取openId失败
            throw new BizInfoException(CommonErrorCode.SYSTEM_ERROR, "error.channel.douyin.authFailed", "openId is blank");
        }
        return new AuthResult()
                .setOpenId(data.getOpenId())
                .setAccessToken(data.getAccessToken());
    }

    /// 按档位 + 主键加载抖音网站应用凭证(含解密后的 appSecret)
    private DyAppView loadApp(String dyAppScope, Long dyAppRefId) {
        if (StrUtil.isBlank(dyAppScope) || dyAppRefId == null) {
            // 抖音: 认证应用引用缺失(dyAppScope/dyAppRefId 未传入)
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.payment.douyin.appNotConfigured", "auth");
        }
        DyAppScopeEnum scope = DyAppScopeEnum.findByCode(dyAppScope);
        DyAppView app = douyinAppFacade.getById(scope, dyAppRefId);
        if (StrUtil.isBlank(app.appSecret())) {
            // 抖音: 直连应用授权密钥未配置
            throw new BizInfoException(CommonErrorCode.SYSTEM_ERROR, "error.channel.douyin.appAuthSecretMissing");
        }
        return app;
    }
}
