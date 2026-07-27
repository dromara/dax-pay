package cn.daxpay.open.channel.douyin.strategy.direct.auth;

import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectApp;
import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectAppAuthConfig;
import cn.daxpay.open.channel.douyin.service.direct.DouyinDirectAppAuthConfigService;
import cn.daxpay.open.channel.douyin.service.direct.DouyinDirectAppCapabilityService;
import cn.daxpay.open.payment.auth.AuthRedirectUri;
import cn.daxpay.open.payment.auth.AuthSession;
import cn.daxpay.open.payment.strategy.auth.AbsChannelAuthStrategy;
import cn.daxpay.open.payment.strategy.auth.AuthContext;
import cn.daxpay.open.payment.unipay.param.assist.AuthCodeParam;
import cn.daxpay.open.payment.unipay.param.assist.GenerateAuthUrlParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.daxpay.open.platform.capability.douyin.auth.result.DouyinAuthResult;
import cn.daxpay.open.platform.capability.douyin.auth.service.DouyinH5AuthService;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 抖音直连认证策略
///
/// 抖音直连模式(DOUYIN_PAY)下获取用户标识(openId)。复用支付的商户配置体系定位直连应用
/// (DouyinDirectApp + DouyinDirectAppAuthConfig), 调用 capability-douyin 完成 H5 silent_auth。
///
/// H5 silent_auth 强制解析网站应用, 见 [DouyinDirectAppCapabilityService#resolveWebAppForH5Auth]。
@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinDirectAuthStrategy extends AbsChannelAuthStrategy {

    private final DouyinH5AuthService douyinH5AuthService;
    private final DouyinDirectAppCapabilityService douyinDirectAppCapabilityService;
    private final DouyinDirectAppAuthConfigService douyinDirectAppAuthConfigService;
    private final PlatformUrlConfigService platformUrlConfigService;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.DOUYIN_PAY;
    }

    /// 生成抖音 H5 静默授权链接
    @Override
    public AuthUrlResult generateAuthUrl(GenerateAuthUrlParam param, String authToken) {
        DouyinDirectApp app = douyinDirectAppCapabilityService.resolveWebAppForH5Auth(
                param.getChannelMchNo(), param.getCapability(), param.getChannelAppId());
        DouyinDirectAppAuthConfig authConfig = douyinDirectAppAuthConfigService.findByDouyinDirectAppIdForAuth(app.getId());
        if (StrUtil.isBlank(authConfig.getAppSecret())) {
            // 抖音: 直连应用授权密钥未配置
            throw new BizInfoException(CommonErrorCode.SYSTEM_ERROR, "error.channel.douyin.appAuthSecretMissing");
        }
        String redirectUri = AuthRedirectUri.DOUYIN.buildRedirectUri(platformUrlConfigService);
        String authUrl = douyinH5AuthService.buildSilentAuthUrl(
                app.getDouyinAppId(), redirectUri, authToken);
        return new AuthUrlResult().setAuthUrl(authUrl);
    }

    /// 通过授权 code 换取 openId
    @Override
    public AuthResult doAuth(AuthCodeParam param, AuthSession session) {
        AuthContext ctx = resolveContext(param, session);
        DouyinDirectApp app = douyinDirectAppCapabilityService.resolveWebAppForH5Auth(
                ctx.channelMchNo(), ctx.capability(), ctx.channelAppId());
        DouyinDirectAppAuthConfig authConfig = douyinDirectAppAuthConfigService.findByDouyinDirectAppIdForAuth(app.getId());
        if (StrUtil.isBlank(authConfig.getAppSecret())) {
            // 抖音: 直连应用授权密钥未配置
            throw new BizInfoException(CommonErrorCode.SYSTEM_ERROR, "error.channel.douyin.appAuthSecretMissing");
        }
        DouyinAuthResult data = douyinH5AuthService.getOpenIdByCode(
                app.getDouyinAppId(), authConfig.getAppSecret(), param.getAuthCode());
        if (StrUtil.isBlank(data.getOpenId())) {
            // 抖音: 获取openId失败
            throw new BizInfoException(CommonErrorCode.SYSTEM_ERROR, "error.channel.douyin.authFailed", "openId is blank");
        }
        return new AuthResult()
                .setOpenId(data.getOpenId())
                .setAccessToken(data.getAccessToken());
    }

}
