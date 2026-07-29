package cn.daxpay.open.payment.trade.runtime.service.pay.gateway;

import cn.daxpay.open.platform.capability.alipay.auth.config.AlipayAuthConfig;
import cn.daxpay.open.platform.capability.alipay.auth.result.AlipayAuthResult;
import cn.daxpay.open.platform.capability.alipay.auth.service.AlipayAuthCapability;
import cn.daxpay.open.platform.capability.douyin.auth.result.DouyinAuthResult;
import cn.daxpay.open.platform.capability.douyin.auth.service.DouyinMaAuthService;
import cn.daxpay.open.platform.capability.wechat.auth.result.WechatAuthResult;
import cn.daxpay.open.platform.capability.wechat.auth.service.WechatMaAuthService;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.unipay.ChannelAuthStatusEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.system.enums.MobilePlatformEnum;
import cn.daxpay.open.platform.system.mobile.config.AlipayMiniAppConfig;
import cn.daxpay.open.platform.system.mobile.config.DyMiniAppConfig;
import cn.daxpay.open.platform.system.mobile.config.WxMiniAppConfig;
import cn.daxpay.open.platform.system.service.mobile.MobileAppService;
import cn.daxpay.open.payment.unipay.param.gateway.CashierAuthParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 收银台小程序认证服务
///
/// 收银台小程序(微信/支付宝/抖音)前端通过 uni.login / my.getAuthCode / tt.login
/// 获取授权码后, 本服务按通道分发到对应 capability 能力层换取 openId/userId。
///
/// 与 H5 网关认证([GatewayAuthService] + [cn.daxpay.open.payment.auth.UnifiedAuthService]) 不同:
/// - H5: OAuth 跳转 → 异步 session/queryCode 轮询
/// - 小程序: 前端直拿 code → 同步直返 openId, 无需 session 机制
///
/// 配置来源: [MobileAppService#findAppConfig] 从 pay_platform_mobile_app 表
/// 按 (appType=cashier, platform) 维度读取, AES 自动解密。
@Slf4j
@Service
@RequiredArgsConstructor
public class CashierAuthService {

    /// 收银台端类型(对齐 MobileAppTypeEnum.CASHIER)
    private static final String APP_TYPE_CASHIER = "cashier";

    private final MobileAppService mobileAppService;
    private final WechatMaAuthService wechatMaAuthService;
    private final AlipayAuthCapability alipayAuthCapability;
    private final DouyinMaAuthService douyinMaAuthService;

    /// 收银台小程序认证: 用 authCode 换 openId/userId
    ///
    /// 按 channel 映射到 mobile-app 平台编码, 读取对应配置后分发到 capability 层。
    /// 微信/抖音返回 openId, 支付宝返回 userId(同时回填 openId)。
    public AuthResult auth(CashierAuthParam param) {
        String channel = param.getChannel();
        String authCode = param.getAuthCode();
        return switch (channel) {
            case "wechat" -> authWechat(authCode);
            case "alipay" -> authAlipay(authCode);
            case "douyin" -> authDouyin(authCode);
            default -> throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.mobile_app.platformNotFound", channel);
        };
    }

    /// 微信小程序: jscode2session 换 openId
    private AuthResult authWechat(String authCode) {
        // 读取收银台微信小程序配置
        WxMiniAppConfig config = mobileAppService.findAppConfig(
                APP_TYPE_CASHIER, MobilePlatformEnum.WX_MINI.getCode(), WxMiniAppConfig.class);
        WechatAuthResult data = wechatMaAuthService.getOpenId(authCode, config.getAppId(), config.getAppSecret());
        if (StrUtil.isBlank(data.getOpenId())) {
            // 微信: 获取openId失败
            throw new BizInfoException(CommonErrorCode.SYSTEM_ERROR, "error.channel.wechat.authFailed", "openId is blank");
        }
        return new AuthResult()
                .setOpenId(data.getOpenId())
                .setAccessToken(data.getAccessToken())
                .setStatus(ChannelAuthStatusEnum.SUCCESS.getCode());
    }

    /// 支付宝小程序: alipay.system.oauth.token 换 userId
    private AuthResult authAlipay(String authCode) {
        // 读取收银台支付宝小程序配置
        AlipayMiniAppConfig config = mobileAppService.findAppConfig(
                APP_TYPE_CASHIER, MobilePlatformEnum.ALIPAY_MINI.getCode(), AlipayMiniAppConfig.class);
        AlipayAuthConfig authConfig = toAlipayAuthConfig(config);
        AlipayAuthResult alipayResult = alipayAuthCapability.getUserId(authConfig, authCode);
        // 优先 userId(传统), 其次 openId(新标准)
        String userId = StrUtil.blankToDefault(alipayResult.getUserId(), alipayResult.getOpenId());
        if (StrUtil.isBlank(userId)) {
            // 支付宝: 获取用户标识失败
            throw new BizInfoException(CommonErrorCode.SYSTEM_ERROR, "error.alipay.authFailed", "userId is blank");
        }
        return new AuthResult()
                .setOpenId(userId)
                .setUserId(userId)
                .setAccessToken(alipayResult.getAccessToken())
                .setStatus(ChannelAuthStatusEnum.SUCCESS.getCode());
    }

    /// 抖音小程序: jscode2session 换 openId
    private AuthResult authDouyin(String authCode) {
        // 读取收银台抖音小程序配置
        DyMiniAppConfig config = mobileAppService.findAppConfig(
                APP_TYPE_CASHIER, MobilePlatformEnum.DY_MINI.getCode(), DyMiniAppConfig.class);
        DouyinAuthResult data = douyinMaAuthService.getOpenId(
                config.getAppId(), config.getAppSecret(), authCode);
        if (StrUtil.isBlank(data.getOpenId())) {
            // 抖音: 获取用户标识失败
            throw new BizInfoException(CommonErrorCode.SYSTEM_ERROR, "error.douyin.authFailed", "openId is blank");
        }
        return new AuthResult()
                .setOpenId(data.getOpenId())
                .setStatus(ChannelAuthStatusEnum.SUCCESS.getCode());
    }

    /// AlipayMiniAppConfig → capability 层 AlipayAuthConfig(字段一一对应)
    private AlipayAuthConfig toAlipayAuthConfig(AlipayMiniAppConfig config) {
        return new AlipayAuthConfig()
                .setAppId(config.getAppId())
                .setAuthType(config.getAuthType())
                .setPrivateKey(config.getPrivateKey())
                .setAlipayPublicKey(config.getAlipayPublicKey())
                .setAppCert(config.getAppCert())
                .setAlipayCert(config.getAlipayCert())
                .setAlipayRootCert(config.getAlipayRootCert());
    }
}
