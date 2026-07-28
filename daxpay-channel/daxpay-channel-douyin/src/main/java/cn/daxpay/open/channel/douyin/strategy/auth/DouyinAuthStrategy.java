package cn.daxpay.open.channel.douyin.strategy.auth;

import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectApp;
import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectAppAuthConfig;
import cn.daxpay.open.channel.douyin.service.direct.DouyinDirectAppAuthConfigService;
import cn.daxpay.open.channel.douyin.service.direct.DouyinDirectAppCapabilityService;
import cn.daxpay.open.payment.auth.core.AuthRedirectUri;
import cn.daxpay.open.payment.auth.core.AuthSession;
import cn.daxpay.open.payment.auth.merchant.ChannelAuthStrategy;
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
/// 抖音 H5 silent_auth 取 openId。复用支付的商户配置体系定位直连应用
/// (DouyinDirectApp + DouyinDirectAppAuthConfig), 调用 capability-douyin 完成静默授权。
///
/// 抖音应用体系与微信不同(不使用 WxAppFacade), 策略内部自行从 channelMchNo 解析抖音应用。
/// H5 silent_auth 固定解析网站应用, 见 [DouyinDirectAppCapabilityService#resolveWebAppForH5Auth]。
@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinAuthStrategy implements ChannelAuthStrategy {

    private final DouyinH5AuthService douyinH5AuthService;
    private final DouyinDirectAppCapabilityService douyinDirectAppCapabilityService;
    private final DouyinDirectAppAuthConfigService douyinDirectAppAuthConfigService;
    private final PlatformUrlConfigService platformUrlConfigService;

    @Override
    public ChannelAuthTypeEnum getAuthType() {
        return ChannelAuthTypeEnum.DOUYIN;
    }

    /// 生成抖音 H5 静默授权链接
    @Override
    public AuthUrlResult generateAuthUrl(GenerateAuthUrlParam param, String authToken, AuthSession session) {
        DouyinDirectApp app = resolveApp(param.getChannelMchNo());
        String redirectUri = AuthRedirectUri.DOUYIN.buildRedirectUri(platformUrlConfigService);
        String authUrl = douyinH5AuthService.buildSilentAuthUrl(
                app.getDouyinAppId(), redirectUri, authToken);
        return new AuthUrlResult().setAuthUrl(authUrl);
    }

    /// 通过授权 code 换取 openId
    @Override
    public AuthResult doAuth(AuthCodeParam param, AuthSession session) {
        // channelMchNo 优先从 session 恢复(H5 回调场景), 否则取 param
        String channelMchNo = session != null && StrUtil.isNotBlank(session.getChannelMchNo())
                ? session.getChannelMchNo() : param.getChannelMchNo();
        DouyinDirectApp app = resolveApp(channelMchNo);
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

    /// 解析抖音直连网站应用(H5 silent_auth 固定走网站应用)
    private DouyinDirectApp resolveApp(String channelMchNo) {
        return douyinDirectAppCapabilityService.resolveWebAppForH5Auth(channelMchNo, null, null);
    }
}