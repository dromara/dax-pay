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
import cn.daxpay.open.platform.core.enums.unipay.ChannelAuthTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.system.enums.MobileAppTypeEnum;
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

    private final MobileAppService mobileAppService;
    private final WechatMaAuthService wechatMaAuthService;
    private final AlipayAuthCapability alipayAuthCapability;
    private final DouyinMaAuthService douyinMaAuthService;

    /// 收银台小程序认证: 用 authCode 换 openId/userId
    ///
    /// 按 [ChannelAuthTypeEnum] 分发到对应通道, 与 H5 认证([GatewayAuthService])共用同一套通道枚举。
    /// 微信/抖音返回 openId, 支付宝返回 userId(同时回填 openId)。
    public AuthResult auth(CashierAuthParam param) {
        // channel 字符串 → 枚举(非法值由 findByCode 抛 ConfigNotExistException)
        ChannelAuthTypeEnum authType = ChannelAuthTypeEnum.findByCode(param.getChannel());
        String authCode = param.getAuthCode();
        return switch (authType) {
            case WECHAT -> authWechat(authType, authCode);
            case ALIPAY -> authAlipay(authType, authCode);
            case DOUYIN -> authDouyin(authType, authCode);
            default -> throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.mobile_app.platformNotFound", authType.getCode());
        };
    }

    /// 微信小程序: jscode2session 换 openId
    private AuthResult authWechat(ChannelAuthTypeEnum authType, String authCode) {
        WxMiniAppConfig config = loadConfig(authType, WxMiniAppConfig.class);
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
    private AuthResult authAlipay(ChannelAuthTypeEnum authType, String authCode) {
        AlipayMiniAppConfig config = loadConfig(authType, AlipayMiniAppConfig.class);
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
    private AuthResult authDouyin(ChannelAuthTypeEnum authType, String authCode) {
        DyMiniAppConfig config = loadConfig(authType, DyMiniAppConfig.class);
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

    /// 加载收银台小程序配置(统一入口, 内部完成 channel→platform 映射)
    private <T> T loadConfig(ChannelAuthTypeEnum authType, Class<T> configType) {
        return mobileAppService.findAppConfig(
                MobileAppTypeEnum.CASHIER.getCode(), toMiniPlatform(authType).getCode(), configType);
    }

    /// 认证类型 → 收银台小程序平台编码映射
    private MobilePlatformEnum toMiniPlatform(ChannelAuthTypeEnum authType) {
        return switch (authType) {
            case WECHAT -> MobilePlatformEnum.WX_MINI;
            case ALIPAY -> MobilePlatformEnum.ALIPAY_MINI;
            case DOUYIN -> MobilePlatformEnum.DY_MINI;
            default -> throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.mobile_app.platformNotFound", authType.getCode());
        };
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
