package cn.daxpay.open.payment.core.assist;

import cn.daxpay.open.payment.unipay.param.assist.AuthCodeParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.daxpay.open.platform.capability.alipay.auth.config.AlipayAuthConfig;
import cn.daxpay.open.platform.capability.alipay.auth.result.AlipayAuthResult;
import cn.daxpay.open.platform.capability.alipay.auth.service.AlipayAuthCapability;
import cn.daxpay.open.platform.capability.wechat.auth.result.WechatAuthResult;
import cn.daxpay.open.platform.capability.wechat.auth.result.WechatAuthUrlResult;
import cn.daxpay.open.platform.capability.wechat.auth.service.WechatMpAuthService;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.unipay.ChannelAuthStatusEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.system.entity.config.platform.PlatformWechatMpAuthConfig;
import cn.daxpay.open.platform.system.service.config.PlatformAlipayAuthConfigService;
import cn.daxpay.open.platform.system.service.config.PlatformWechatMpAuthConfigService;
import cn.daxpay.open.platform.system.service.config.PlatformUrlConfigService;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 平台级认证服务
///
/// 承载不依赖商户上下文、由平台级配置驱动的认证场景:
/// - **支付宝**: 平台级支付宝配置 + H5 中间页 JSAPI 取码(调试/支付共用), 不依赖商户上下文
/// - **微信系统公众号配置**: 平台级微信公众号配置([PlatformWechatMpAuthConfig]), OAuth 重定向, 仅调试场景
///
/// 与按支付产品路由策略的 [ChannelAuthService] 解耦: 本服务不读取商户级通道配置,
/// 只消费平台级配置并调用 capability(alipay/wechat) 完成换票; 会话与结果缓存委托 [AuthSessionStore]。
@Slf4j
@Service
@RequiredArgsConstructor
public class PlatformAuthService {

    /// 支付宝 H5 中间页路径: /auth/alipay/{aliAppId}/{queryCode}
    private static final String ALIPAY_AUTH_PATH = "/auth/alipay/{}/{}";

    /// 微信 OAuth 认证回调路径: /auth/wechat/{authToken}
    private static final String WECHAT_AUTH_PATH = "/auth/wechat/{}";

    private final AuthSessionStore authSessionStore;
    private final PlatformAlipayAuthConfigService platformAlipayAuthConfigService;
    private final PlatformWechatMpAuthConfigService platformWechatMpAuthConfigService;
    private final PlatformUrlConfigService platformUrlConfigService;
    private final AlipayAuthCapability alipayAuthCapability;
    private final WechatMpAuthService wechatMpAuthService;

    /// 生成支付宝授权中间页链接(平台级, 无商户上下文)
    ///
    /// 二维码指向 H5: `{paymentGatewayBaseUrl}/auth/alipay/{aliAppId}/{queryCode}`,
    /// H5 内通过 JSAPI `ap.getAuthCode` 取码后回调 [authAlipay]。
    public AuthUrlResult generateAlipayAuthUrl() {
        AlipayAuthConfig config = platformAlipayAuthConfigService.toCapabilityConfig();
        if (!alipayAuthCapability.isConfigured(config)) {
            // 支付宝: 平台级支付宝配置不完整, 请先在「三方平台管理」中配置
            throw new BizInfoException(CommonErrorCode.SYSTEM_ERROR, "error.social.alipayNotConfigured");
        }
        String gatewayBase = platformUrlConfigService.getUrlConfig().getPaymentGatewayBaseUrl();
        if (StrUtil.isBlank(gatewayBase)) {
            // 支付网关前端地址未配置
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.gatewayUrlNotConfigured");
        }
        String queryCode = RandomUtil.randomString(10);
        String authPath = StrUtil.format(ALIPAY_AUTH_PATH, config.getAppId(), queryCode);
        String authUrl = StrUtil.removeSuffix(gatewayBase, "/") + authPath;
        authSessionStore.saveWaitingResult(queryCode);
        return new AuthUrlResult().setAuthUrl(authUrl).setQueryCode(queryCode);
    }

    /// 生成微信系统公众号配置授权链接(平台级, 无商户上下文, 仅调试)
    ///
    /// 读平台级 [PlatformWechatMpAuthConfig], 调用 capability-wechat 生成微信公众号 OAuth 授权链接,
    /// 回调指向 `/auth/wechat/{authToken}`。session 标记 `source=platform_mp`,
    /// 认证分发层据此走平台级微信换票分支([authWechatMp])。
    public AuthUrlResult generateWechatMpAuthUrl() {
        PlatformWechatMpAuthConfig config = platformWechatMpAuthConfigService.getWechatMpAuthConfig();
        if (!isWechatMpConfigured(config)) {
            // 微信: 平台级微信公众号配置不完整, 请先在「平台配置」中配置
            throw new BizInfoException(CommonErrorCode.SYSTEM_ERROR, "error.social.wechatMpNotConfigured");
        }
        String gatewayBase = platformUrlConfigService.getUrlConfig().getPaymentGatewayBaseUrl();
        if (StrUtil.isBlank(gatewayBase)) {
            // 支付网关前端地址未配置
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.gatewayUrlNotConfigured");
        }
        String authToken = IdUtil.fastSimpleUUID();
        String queryCode = RandomUtil.randomString(10);
        AuthSession session = new AuthSession()
                .setSource(AuthSession.SOURCE_PLATFORM_MP)
                .setQueryCode(queryCode);
        authSessionStore.saveSession(authToken, session);
        String redirectUri = StrUtil.removeSuffix(gatewayBase, "/") + StrUtil.format(WECHAT_AUTH_PATH, authToken);
        WechatAuthUrlResult result = wechatMpAuthService.generateAuthUrl(redirectUri, config.getAppId(), config.getAppSecret());
        authSessionStore.saveWaitingResult(queryCode);
        return new AuthUrlResult().setAuthUrl(result.getAuthUrl()).setQueryCode(queryCode);
    }

    /// 支付宝 authCode 换 userId/openId(平台级配置)
    ///
    /// 统一映射: 支付链路按 openId 取值, 同时回填 userId; 结果落库供 queryCode 轮询。
    public AuthResult authAlipay(AuthCodeParam param) {
        AlipayAuthConfig config = platformAlipayAuthConfigService.toCapabilityConfig();
        if (!alipayAuthCapability.isConfigured(config)) {
            // 支付宝: 平台级支付宝配置不完整, 请先在「三方平台管理」中配置
            throw new BizInfoException(CommonErrorCode.SYSTEM_ERROR, "error.social.alipayNotConfigured");
        }
        AlipayAuthResult alipayResult = alipayAuthCapability.getUserId(config, param.getAuthCode());
        String userId = StrUtil.blankToDefault(alipayResult.getUserId(), alipayResult.getOpenId());
        if (StrUtil.isBlank(userId)) {
            // 支付宝: 获取用户标识失败
            throw new BizInfoException(CommonErrorCode.SYSTEM_ERROR, "error.alipay.authFailed", "userId is blank");
        }
        AuthResult authResult = new AuthResult()
                .setOpenId(userId)
                .setUserId(userId)
                .setAccessToken(alipayResult.getAccessToken())
                .setStatus(ChannelAuthStatusEnum.SUCCESS.getCode());
        authSessionStore.writeResultByQueryCode(param.getQueryCode(), null, authResult);
        return authResult;
    }

    /// 微信系统公众号配置平台级换票(读 [PlatformWechatMpAuthConfig])
    public AuthResult authWechatMp(AuthCodeParam param, AuthSession session) {
        PlatformWechatMpAuthConfig config = platformWechatMpAuthConfigService.getWechatMpAuthConfig();
        if (!isWechatMpConfigured(config)) {
            // 微信: 平台级微信公众号配置不完整, 请先在「平台配置」中配置
            throw new BizInfoException(CommonErrorCode.SYSTEM_ERROR, "error.social.wechatMpNotConfigured");
        }
        WechatAuthResult data = wechatMpAuthService.getTokenAndOpenId(param.getAuthCode(), config.getAppId(), config.getAppSecret());
        if (StrUtil.isBlank(data.getOpenId())) {
            // 微信: 获取openId失败
            throw new BizInfoException(CommonErrorCode.SYSTEM_ERROR, "error.channel.wechat.authFailed", "openId is blank");
        }
        AuthResult authResult = new AuthResult()
                .setOpenId(data.getOpenId())
                .setAccessToken(data.getAccessToken())
                .setStatus(ChannelAuthStatusEnum.SUCCESS.getCode());
        authSessionStore.writeResultByQueryCode(param.getQueryCode(), session, authResult);
        return authResult;
    }

    /// 微信系统公众号配置是否完整(appId/appSecret 均非空)
    private boolean isWechatMpConfigured(PlatformWechatMpAuthConfig config) {
        return StrUtil.isNotBlank(config.getAppId()) && StrUtil.isNotBlank(config.getAppSecret());
    }
}
