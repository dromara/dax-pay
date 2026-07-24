package cn.daxpay.open.channel.wechat.strategy.direct.auth;

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

/// # 微信直连认证策略
///
/// 微信直连模式(WECHAT_PAY)下获取用户标识(openId)。通过 [WxAppFacade#resolve]
/// 从主数据解析 wxAppId + appSecret, 调用 capability-wechat 完成 OAuth。
///
/// ## appId 解析
/// 委托 facade: channelAppId 显式指定 → 通道能力绑 → 平台默认(直连期望商户档)。
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatDirectAuthStrategy extends AbsChannelAuthStrategy {

    private final WechatMpAuthService wechatMpAuthService;
    private final WxAppFacade wxAppFacade;
    private final ChannelMerchantManager channelMerchantManager;
    private final PlatformUrlConfigService platformUrlConfigService;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.WECHAT_PAY;
    }

    /// 生成公众号 OAuth 授权链接
    ///
    /// 拼接回调地址({paymentGatewayBaseUrl}/auth/wechat, 固定路径), 委托 capability-wechat 生成微信 OAuth URL。
    /// 会话标识 authToken 通过 OAuth state 参数透传。
    @Override
    public AuthUrlResult generateAuthUrl(GenerateAuthUrlParam param, String authToken) {
        WxAppView app = resolveApp(param.getMchNo(), param.getChannelMchNo(),
                param.getCapability(), param.getChannelAppId());
        String redirectUri = buildRedirectUri();
        WechatAuthUrlResult result = wechatMpAuthService.generateAuthUrl(
                redirectUri, app.wxAppId(), app.appSecret(), authToken);
        return new AuthUrlResult().setAuthUrl(result.getAuthUrl());
    }

    /// 通过授权 code(公众号 OAuth 回调的 code)换取 openId
    ///
    /// 应用上下文优先从 session(H5 会话码场景)恢复, 否则取 param(小程序直连场景)。
    @Override
    public AuthResult doAuth(AuthCodeParam param, AuthSession session) {
        String channelMchNo = session != null && StrUtil.isNotBlank(session.getChannelMchNo())
                ? session.getChannelMchNo() : param.getChannelMchNo();
        String capability = session != null && StrUtil.isNotBlank(session.getCapability())
                ? session.getCapability() : param.getCapability();
        String channelAppId = session != null && StrUtil.isNotBlank(session.getChannelAppId())
                ? session.getChannelAppId() : param.getChannelAppId();
        WxAppView app = resolveApp(param.getMchNo(), channelMchNo, capability, channelAppId);
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

    /// 解析认证应用: 主数据 facade.resolve(wxAppId + 明文 appSecret)
    private WxAppView resolveApp(String mchNo, String channelMchNo, String capability, String channelAppId) {
        String resolvedMchNo = resolveMchNo(mchNo, channelMchNo);
        return wxAppFacade.resolve(resolvedMchNo, channelMchNo, capability, channelAppId,
                ProductEnum.WECHAT_PAY.getCode());
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
}
