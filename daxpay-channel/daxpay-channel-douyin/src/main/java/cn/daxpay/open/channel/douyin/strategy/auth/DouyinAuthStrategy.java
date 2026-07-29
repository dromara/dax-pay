package cn.daxpay.open.channel.douyin.strategy.auth;

import cn.daxpay.open.payment.auth.core.AuthRedirectUri;
import cn.daxpay.open.payment.auth.core.AuthSession;
import cn.daxpay.open.payment.auth.channel.ChannelAuthStrategy;
import cn.daxpay.open.payment.douyin.facade.DouyinAppFacade;
import cn.daxpay.open.payment.douyin.facade.DyAppView;
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
/// 抖音 H5 silent_auth 取 openId。通过 [DouyinAppFacade#resolveWebAppForH5Auth] 解析商户/平台档
/// 网站应用(web_app), 调用 capability-douyin 完成静默授权。应用主数据已上移至商户/平台级,
/// 通道商户下不再持有抖音应用。
///
/// H5 silent_auth 固定解析网站应用(web_app), 与支付能力(按 capability 解析的小程序/移动应用)不同。
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
    /// 据商户/通道商户定位网站应用, 拼接回调地址, 委托 capability-douyin 生成授权 URL。
    /// 同时把 mchNo/channelMchNo 写入 session, 供回调 doAuth 恢复应用解析上下文。
    @Override
    public AuthUrlResult generateAuthUrl(GenerateAuthUrlParam param, String authToken, AuthSession session) {
        DyAppView app = douyinAppFacade.resolveWebAppForH5Auth(
                param.getMchNo(), param.getChannelMchNo(), null);
        // 写入上下文供回调 doAuth 恢复
        session.setMchNo(param.getMchNo());
        session.setChannelMchNo(param.getChannelMchNo());
        String redirectUri = AuthRedirectUri.DOUYIN.buildRedirectUri(platformUrlConfigService);
        String authUrl = douyinH5AuthService.buildSilentAuthUrl(
                app.douyinAppId(), redirectUri, authToken);
        return new AuthUrlResult().setAuthUrl(authUrl);
    }

    /// 通过授权 code 换取 openId
    ///
    /// 据 session 恢复的 mchNo/channelMchNo 重新解析网站应用, 用 code 换 openId。
    @Override
    public AuthResult doAuth(AuthCodeParam param, AuthSession session) {
        // mchNo/channelMchNo 优先从 session 恢复(H5 回调场景), 否则取 param
        String mchNo = session != null && StrUtil.isNotBlank(session.getMchNo())
                ? session.getMchNo() : param.getMchNo();
        String channelMchNo = session != null && StrUtil.isNotBlank(session.getChannelMchNo())
                ? session.getChannelMchNo() : param.getChannelMchNo();
        DyAppView app = douyinAppFacade.resolveWebAppForH5Auth(mchNo, channelMchNo, null);
        if (StrUtil.isBlank(app.appSecret())) {
            // 抖音: 直连应用授权密钥未配置
            throw new BizInfoException(CommonErrorCode.SYSTEM_ERROR, "error.channel.douyin.appAuthSecretMissing");
        }
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
}
