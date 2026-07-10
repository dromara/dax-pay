package cn.daxpay.open.channel.wechat.strategy.isv;

import cn.daxpay.open.channel.wechat.code.WechatAuthAppTypeEnum;
import cn.daxpay.open.channel.wechat.dao.isv.WechatIsvAppManager;
import cn.daxpay.open.channel.wechat.dao.isv.WechatIsvChannelMerchantManager;
import cn.daxpay.open.channel.wechat.dao.isv.WechatIsvMchAppManager;
import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvApp;
import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvAppAuthConfig;
import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvChannelMerchant;
import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvMchApp;
import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvMchAppAuthConfig;
import cn.daxpay.open.channel.wechat.service.isv.WechatIsvAppAuthConfigService;
import cn.daxpay.open.channel.wechat.service.isv.WechatIsvAppCapabilityService;
import cn.daxpay.open.channel.wechat.service.isv.WechatIsvMchAppAuthConfigService;
import cn.daxpay.open.channel.wechat.service.isv.WechatIsvMchAppCapabilityService;
import cn.daxpay.open.payment.core.assist.AuthSession;
import cn.daxpay.open.payment.core.strategy.auth.AbsChannelAuthStrategy;
import cn.daxpay.open.payment.unipay.param.assist.AuthCodeParam;
import cn.daxpay.open.payment.unipay.param.assist.GenerateAuthUrlParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
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
/// ([cn.daxpay.open.channel.wechat.entity.isv.WechatIsvChannelMerchant.authAppType]) 路由:
///
/// - **SP_APP(默认)**: 用服务商应用(WechatIsvApp.sp_appid) + WechatIsvAppAuthConfig 认证, 所得为 sp 维度 openId
/// - **SUB_APP**: 用子商户应用(WechatIsvMchApp.sub_appid) + WechatIsvMchAppAuthConfig 认证, 所得为 sub 维度 openId
///
/// ## appId 解析优先级
/// 1. opAppId 显式指定 → 按 authAppType 查对应表校验预配, 命中即用
/// 2. 否则按 authAppType + capability 自动解析
///
/// 微信 OAuth 调用 capability-wechat, 与支付通道子应用解耦; 区别仅是主应用传入的 wxAppId/appSecret 来源不同。
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatIsvAuthStrategy extends AbsChannelAuthStrategy {

    private final WechatMpAuthService wechatMpAuthService;
    private final WechatIsvAppCapabilityService wechatIsvAppCapabilityService;
    private final WechatIsvAppAuthConfigService wechatIsvAppAuthConfigService;
    private final WechatIsvAppManager wechatIsvAppManager;
    private final WechatIsvMchAppCapabilityService wechatIsvMchAppCapabilityService;
    private final WechatIsvMchAppAuthConfigService wechatIsvMchAppAuthConfigService;
    private final WechatIsvMchAppManager wechatIsvMchAppManager;
    private final WechatIsvChannelMerchantManager wechatIsvChannelMerchantManager;
    private final PlatformUrlConfigService platformUrlConfigService;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.WECHAT_ISV;
    }

    /// 生成公众号 OAuth 授权链接
    @Override
    public AuthUrlResult generateAuthUrl(GenerateAuthUrlParam param, String authToken) {
        ResolvedAuthApp app = resolveAuthApp(param.getChannelMchNo(), param.getCapability(), param.getOpAppId());
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
        String opAppId = session != null && StrUtil.isNotBlank(session.getOpAppId())
                ? session.getOpAppId() : param.getOpAppId();
        ResolvedAuthApp app = resolveAuthApp(channelMchNo, capability, opAppId);
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
    /// 优先级: opAppId(按authAppType校验对应表) > authAppType + capability 自动解析
    private ResolvedAuthApp resolveAuthApp(String channelMchNo, String capability, String opAppId) {
        WechatAuthAppTypeEnum authAppType = loadAuthAppType(channelMchNo);
        if (StrUtil.isNotBlank(opAppId)) {
            return resolveByOpAppId(channelMchNo, opAppId, authAppType);
        }
        return resolveByConfig(channelMchNo, capability, authAppType);
    }

    /// 读取特约商户的认证应用类型, 默认 SP_APP(服务商应用)
    private WechatAuthAppTypeEnum loadAuthAppType(String channelMchNo) {
        return wechatIsvChannelMerchantManager.findByChannelMchNoNotTenant(channelMchNo)
                .map(WechatIsvChannelMerchant::getAuthAppType)
                .filter(StrUtil::isNotBlank)
                .map(WechatAuthAppTypeEnum::valueOf)
                .orElse(WechatAuthAppTypeEnum.SP_APP);
    }

    /// 按配置(authAppType + capability)解析应用
    private ResolvedAuthApp resolveByConfig(String channelMchNo, String capability, WechatAuthAppTypeEnum authAppType) {
        if (authAppType == WechatAuthAppTypeEnum.SP_APP) {
            WechatIsvApp app = wechatIsvAppCapabilityService.resolveApp(capability)
                    // 微信: 服务商应用不存在
                    .orElseThrow(() -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                            "error.channel.wechat.appNotFound"));
            WechatIsvAppAuthConfig cfg = wechatIsvAppAuthConfigService.findByWechatIsvAppId(app.getId());
            return new ResolvedAuthApp(app.getWxAppId(), cfg.getAppSecret());
        }
        // SUB_APP: 子商户应用(仅显式配置, 未配置返回empty)
        WechatIsvMchApp app = wechatIsvMchAppCapabilityService.resolveApp(channelMchNo, capability)
                // 微信: 服务商通道商户应用不存在
                .orElseThrow(() -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.channel.wechat.mchAppNotFound"));
        WechatIsvMchAppAuthConfig cfg = wechatIsvMchAppAuthConfigService.findByWechatIsvMchAppIdForAuth(app.getId());
        return new ResolvedAuthApp(app.getWxAppId(), cfg.getAppSecret());
    }

    /// 按 opAppId 解析应用(按 authAppType 查对应表校验预配)
    private ResolvedAuthApp resolveByOpAppId(String channelMchNo, String opAppId, WechatAuthAppTypeEnum authAppType) {
        if (authAppType == WechatAuthAppTypeEnum.SP_APP) {
            WechatIsvApp app = wechatIsvAppManager.findByWxAppId(opAppId)
                    .orElseThrow(() -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                            "error.channel.wechat.opAppIdNotFound", opAppId));
            WechatIsvAppAuthConfig cfg = wechatIsvAppAuthConfigService.findByWechatIsvAppId(app.getId());
            return new ResolvedAuthApp(app.getWxAppId(), cfg.getAppSecret());
        }
        WechatIsvMchApp app = wechatIsvMchAppManager.findByChannelMchNoAndWxAppIdNotTenant(channelMchNo, opAppId)
                .orElseThrow(() -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.channel.wechat.opAppIdNotFound", opAppId));
        WechatIsvMchAppAuthConfig cfg = wechatIsvMchAppAuthConfigService.findByWechatIsvMchAppIdForAuth(app.getId());
        return new ResolvedAuthApp(app.getWxAppId(), cfg.getAppSecret());
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
