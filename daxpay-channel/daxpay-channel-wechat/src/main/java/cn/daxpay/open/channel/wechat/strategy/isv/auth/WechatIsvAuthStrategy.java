package cn.daxpay.open.channel.wechat.strategy.isv.auth;

import cn.daxpay.open.channel.wechat.code.WechatAuthAppTypeEnum;
import cn.daxpay.open.channel.wechat.dao.isv.WechatIsvChannelMerchantManager;
import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvChannelMerchant;
import cn.daxpay.open.payment.auth.AuthSession;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.payment.strategy.auth.AbsChannelAuthStrategy;
import cn.daxpay.open.payment.unipay.param.assist.AuthCodeParam;
import cn.daxpay.open.payment.unipay.param.assist.GenerateAuthUrlParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.daxpay.open.payment.wx.facade.WxAppFacade;
import cn.daxpay.open.payment.wx.facade.WxAppView;
import cn.daxpay.open.payment.wx.facade.WxIsvAppPair;
import cn.daxpay.open.platform.capability.wechat.auth.result.WechatAuthResult;
import cn.daxpay.open.platform.capability.wechat.auth.result.WechatAuthUrlResult;
import cn.daxpay.open.platform.capability.wechat.auth.service.WechatMpAuthService;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 微信服务商认证策略
///
/// 微信服务商模式(WECHAT_ISV)下获取用户标识(openId)。按特约商户配置的认证应用类型
/// ([cn.daxpay.open.channel.wechat.entity.isv.WechatIsvChannelMerchant#authAppType]) 路由:
///
/// - **SP_APP(默认)**: 用 [WxAppFacade#resolveIsvPair] 的 platform(sp_appid + Secret) 认证
/// - **SUB_APP**: 用 pair.merchant()(sub_appid + Secret) 认证, 未配置抛 mchAppNotFound
///
/// channelAppId 非空时仍走 facade.resolveIsvPair(facade 已处理显式 appId)。
/// wxAppId/appSecret 均来自主数据, 不再读旧 WechatIsvApp* / WechatIsvMchApp* 表。
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatIsvAuthStrategy extends AbsChannelAuthStrategy {

    private final WechatMpAuthService wechatMpAuthService;
    private final WxAppFacade wxAppFacade;
    private final WechatIsvChannelMerchantManager wechatIsvChannelMerchantManager;
    private final ChannelMerchantManager channelMerchantManager;
    private final PlatformUrlConfigService platformUrlConfigService;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.WECHAT_ISV;
    }

    /// 生成公众号 OAuth 授权链接
    @Override
    public AuthUrlResult generateAuthUrl(GenerateAuthUrlParam param, String authToken) {
        ResolvedAuthApp app = resolveAuthApp(param.getMchNo(), param.getChannelMchNo(),
                param.getCapability(), param.getChannelAppId());
        String redirectUri = buildRedirectUri();
        WechatAuthUrlResult result = wechatMpAuthService.generateAuthUrl(
                redirectUri, app.wxAppId(), app.appSecret(), authToken);
        return new AuthUrlResult().setAuthUrl(result.getAuthUrl());
    }

    /// 通过授权 code 换取 openId
    @Override
    public AuthResult doAuth(AuthCodeParam param, AuthSession session) {
        String channelMchNo = session != null && StrUtil.isNotBlank(session.getChannelMchNo())
                ? session.getChannelMchNo() : param.getChannelMchNo();
        String capability = session != null && StrUtil.isNotBlank(session.getCapability())
                ? session.getCapability() : param.getCapability();
        String channelAppId = session != null && StrUtil.isNotBlank(session.getChannelAppId())
                ? session.getChannelAppId() : param.getChannelAppId();
        ResolvedAuthApp app = resolveAuthApp(param.getMchNo(), channelMchNo, capability, channelAppId);
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

    /// 解析认证应用(返回 wxAppId + appSecret)
    ///
    /// SP_APP → pair.platform；SUB_APP → pair.merchant(必填)
    private ResolvedAuthApp resolveAuthApp(String mchNo, String channelMchNo, String capability, String channelAppId) {
        String resolvedMchNo = resolveMchNo(mchNo, channelMchNo);
        WechatAuthAppTypeEnum authAppType = loadAuthAppType(channelMchNo);
        WxIsvAppPair pair = wxAppFacade.resolveIsvPair(resolvedMchNo, channelMchNo, capability, channelAppId,
                ProductEnum.WECHAT_ISV.getCode());
        if (authAppType == WechatAuthAppTypeEnum.SP_APP) {
            WxAppView platform = pair.platform();
            return new ResolvedAuthApp(platform.wxAppId(), platform.appSecret());
        }
        // SUB_APP: 商户档应用必填
        WxAppView merchant = pair.merchant();
        if (merchant == null) {
            // 微信: 服务商通道商户应用不存在
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.wechat.mchAppNotFound");
        }
        return new ResolvedAuthApp(merchant.wxAppId(), merchant.appSecret());
    }

    /// 读取特约商户的认证应用类型, 默认 SP_APP(服务商应用)
    private WechatAuthAppTypeEnum loadAuthAppType(String channelMchNo) {
        return wechatIsvChannelMerchantManager.findByChannelMchNoNotTenant(channelMchNo)
                .map(WechatIsvChannelMerchant::getAuthAppType)
                .filter(StrUtil::isNotBlank)
                .map(WechatAuthAppTypeEnum::valueOf)
                .orElse(WechatAuthAppTypeEnum.SP_APP);
    }

    /// 解析商户号: param 优先, 否则按通道商户号反查
    private String resolveMchNo(String mchNo, String channelMchNo) {
        if (StrUtil.isNotBlank(mchNo)) {
            return mchNo;
        }
        return channelMerchantManager.findByChannelMchNo(channelMchNo)
                .map(ChannelMerchant::getMchNo)
                // 微信: 通道商户不存在
                .orElseThrow(() -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.error.assist.channelMchNotFound", channelMchNo));
    }

    /// 拼接认证回调地址: {paymentGatewayBaseUrl}/auth/wechat
    ///
    /// 固定路径(不含动态段), 会话标识 authToken 通过 OAuth state 参数透传。
    private String buildRedirectUri() {
        String base = platformUrlConfigService.getUrlConfig().getPaymentGatewayBaseUrl();
        if (StrUtil.isBlank(base)) {
            // 支付网关前端地址未配置
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.gatewayUrlNotConfigured");
        }
        return StrUtil.removeSuffix(base, "/") + "/auth/wechat";
    }

    /// 解析后的认证应用(wxAppId + appSecret)
    private record ResolvedAuthApp(String wxAppId, String appSecret) { }
}
