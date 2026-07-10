package cn.daxpay.open.payment.core.assist;

import cn.daxpay.open.payment.unipay.param.assist.AuthCodeParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.daxpay.open.platform.capability.alipay.auth.config.AlipayAuthConfig;
import cn.daxpay.open.platform.capability.alipay.auth.result.AlipayAuthResult;
import cn.daxpay.open.platform.capability.alipay.auth.service.AlipayAuthCapability;
import cn.daxpay.open.platform.capability.douyin.auth.result.DouyinAuthResult;
import cn.daxpay.open.platform.capability.douyin.auth.service.DouyinH5AuthService;
import cn.daxpay.open.platform.capability.wechat.auth.result.WechatAuthResult;
import cn.daxpay.open.platform.capability.wechat.auth.result.WechatAuthUrlResult;
import cn.daxpay.open.platform.capability.wechat.auth.service.WechatMpAuthService;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.unipay.ChannelAuthStatusEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.system.entity.config.platform.auth.PlatformDouyinH5AuthConfig;
import cn.daxpay.open.platform.system.entity.config.platform.auth.PlatformWechatMpAuthConfig;
import cn.daxpay.open.platform.system.service.config.auth.PlatformAlipayAuthConfigService;
import cn.daxpay.open.platform.system.service.config.auth.PlatformDouyinH5AuthConfigService;
import cn.daxpay.open.platform.system.service.config.auth.PlatformWechatMpAuthConfigService;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 平台级认证服务
///
/// 承载不依赖商户上下文、由平台级配置驱动的认证场景:
/// - **支付宝**: 平台级支付宝配置([PlatformAlipayAuthConfig]), OAuth 重定向, 调试/支付共用
/// - **微信系统公众号配置**: 平台级微信公众号配置([PlatformWechatMpAuthConfig]), OAuth 重定向, 仅调试场景
/// - **抖音 H5 应用**: 平台级抖音 H5 配置([PlatformDouyinH5AuthConfig]), silent_auth 静默授权, 仅调试场景
///
/// 与按支付产品路由策略的 [ChannelAuthService] 解耦: 本服务不读取商户级通道配置,
/// 只消费平台级配置并调用 capability(alipay/wechat/douyin) 完成换票; 会话与结果缓存委托 [AuthSessionStore]。
///
/// 三通道统一模式: 固定 redirect_uri + OAuth state 透传 authToken, 回调后从 state 恢复会话。
@Slf4j
@Service
@RequiredArgsConstructor
public class PlatformAuthService {

    /// 支付宝 OAuth 认证回调路径: /auth/alipay
    ///
    /// 固定路径, 需在支付宝开放平台登记为授权回调地址。
    /// 会话标识 authToken 通过 OAuth state 参数透传, 回调后从 state 恢复。
    private static final String ALIPAY_AUTH_PATH = "/auth/alipay";

    /// 微信 OAuth 认证回调路径: /auth/wechat
    ///
    /// 微信公众号 OAuth 的 redirect_uri 只校验域名(不要求精确匹配), 但为统一三通道模式
    /// (微信/抖音/支付宝均用固定 redirect_uri + state 透传会话标识), 这里也改为固定路径。
    /// 会话标识 authToken 通过 OAuth state 参数透传, 回调后从 state 恢复。
    private static final String WECHAT_AUTH_PATH = "/auth/wechat";

    /// 抖音 H5 认证回调路径: /auth/douyin
    ///
    /// 抖音 silent_auth 要求 redirect_uri 与平台配置完全一致(不支持 path 段或 query 参数),
    /// 故为固定路径。会话标识(authToken)通过 state 参数透传, 回调后从 state 中恢复。
    private static final String DOUYIN_AUTH_PATH = "/auth/douyin";

    /// 支付宝授权范围: auth_base(静默授权, 仅取 userId, 不弹确认页)
    private static final String ALIPAY_SCOPE = "auth_base";

    private final AuthSessionStore authSessionStore;
    private final PlatformAlipayAuthConfigService platformAlipayAuthConfigService;
    private final PlatformWechatMpAuthConfigService platformWechatMpAuthConfigService;
    private final PlatformDouyinH5AuthConfigService platformDouyinH5AuthConfigService;
    private final PlatformUrlConfigService platformUrlConfigService;
    private final AlipayAuthCapability alipayAuthCapability;
    private final WechatMpAuthService wechatMpAuthService;
    private final DouyinH5AuthService douyinH5AuthService;

    /// 生成支付宝授权链接(平台级, 无商户上下文)
    ///
    /// 读取平台级 [PlatformAlipayAuthConfig], 调用 capability-alipay 生成支付宝 OAuth 授权链接,
    /// 回调指向固定的 `/auth/alipay`。会话标识 authToken 通过 OAuth state 参数透传, 回调后从 state 恢复会话。
    /// session 标记 `source=platform_alipay`, 认证分发层据此走平台级支付宝换票分支([#authAlipay])。
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
        String authToken = IdUtil.fastSimpleUUID();
        String queryCode = RandomUtil.randomString(10);
        AuthSession session = new AuthSession()
                .setSource(AuthSession.SOURCE_PLATFORM_ALIPAY)
                .setQueryCode(queryCode);
        authSessionStore.saveSession(authToken, session);
        // redirect_uri 为固定路径(需在支付宝开放平台登记), authToken 通过 OAuth state 透传
        String redirectUri = StrUtil.removeSuffix(gatewayBase, "/") + ALIPAY_AUTH_PATH;
        String authUrl = alipayAuthCapability.generateAuthUrl(config, redirectUri, ALIPAY_SCOPE, authToken, false);
        authSessionStore.saveWaitingResult(queryCode);
        return new AuthUrlResult().setAuthUrl(authUrl).setQueryCode(queryCode);
    }

    /// 生成微信系统公众号配置授权链接(平台级, 无商户上下文, 仅调试)
    ///
    /// 读取平台级 [PlatformWechatMpAuthConfig], 调用 capability-wechat 生成微信公众号 OAuth 授权链接,
    /// 回调指向固定的 `/auth/wechat`。会话标识 authToken 通过 OAuth state 参数透传, 回调后从 state 恢复会话。
    /// session 标记 `source=platform_mp`, 认证分发层据此走平台级微信换票分支([#authWechatMp])。
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
        // redirect_uri 为固定路径, authToken 通过 OAuth state 透传
        String redirectUri = StrUtil.removeSuffix(gatewayBase, "/") + WECHAT_AUTH_PATH;
        WechatAuthUrlResult result = wechatMpAuthService.generateAuthUrl(redirectUri, config.getAppId(), config.getAppSecret(), authToken);
        authSessionStore.saveWaitingResult(queryCode);
        return new AuthUrlResult().setAuthUrl(result.getAuthUrl()).setQueryCode(queryCode);
    }

    /// 生成抖音 H5 静默授权链接(平台级, 无商户上下文, 仅调试)
    ///
    /// 读取平台级 [PlatformDouyinH5AuthConfig], 调用 capability-douyin [DouyinH5AuthService] 构造 silent_auth 链接,
    /// 回调指向固定的 `/auth/douyin`(抖音要求 redirect_uri 与平台配置完全一致, 不支持 path 段或 query 参数)。
    /// 会话标识 authToken 通过 state 参数透传, 回调后从 state 恢复会话。
    /// session 标记 `source=platform_douyin`, 认证分发层据此走平台级抖音换票分支([#authDouyin])。
    public AuthUrlResult generateDouyinAuthUrl() {
        PlatformDouyinH5AuthConfig config = platformDouyinH5AuthConfigService.getDouyinH5AuthConfig();
        if (!isDouyinH5Configured(config)) {
            // 抖音: 平台级抖音 H5 应用配置不完整, 请先在「三方平台管理」中配置
            throw new BizInfoException(CommonErrorCode.SYSTEM_ERROR, "error.social.douyinH5NotConfigured");
        }
        String gatewayBase = platformUrlConfigService.getUrlConfig().getPaymentGatewayBaseUrl();
        if (StrUtil.isBlank(gatewayBase)) {
            // 支付网关前端地址未配置
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.gatewayUrlNotConfigured");
        }
        String authToken = IdUtil.fastSimpleUUID();
        String queryCode = RandomUtil.randomString(10);
        AuthSession session = new AuthSession()
                .setSource(AuthSession.SOURCE_PLATFORM_DOUYIN)
                .setQueryCode(queryCode);
        authSessionStore.saveSession(authToken, session);
        // redirect_uri 为固定路径(需与抖音开放平台配置完全一致), authToken 通过 state 透传
        String redirectUri = StrUtil.removeSuffix(gatewayBase, "/") + DOUYIN_AUTH_PATH;
        String authUrl = douyinH5AuthService.buildSilentAuthUrl(config.getClientKey(), redirectUri, authToken);
        authSessionStore.saveWaitingResult(queryCode);
        return new AuthUrlResult().setAuthUrl(authUrl).setQueryCode(queryCode);
    }

    /// 支付宝 authCode 换 userId/openId(平台级配置)
    ///
    /// 统一映射: 支付链路按 openId 取值, 同时回填 userId; 结果落库供 queryCode 轮询。
    /// queryCode 优先从 param 取, 其次从 session 恢复(OAuth 回调场景)。
    public AuthResult authAlipay(AuthCodeParam param, AuthSession session) {
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
        authSessionStore.writeResultByQueryCode(param.getQueryCode(), session, authResult);
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

    /// 抖音 H5 应用配置平台级换票(读 [PlatformDouyinH5AuthConfig])
    public AuthResult authDouyin(AuthCodeParam param, AuthSession session) {
        PlatformDouyinH5AuthConfig config = platformDouyinH5AuthConfigService.getDouyinH5AuthConfig();
        if (!isDouyinH5Configured(config)) {
            // 抖音: 平台级抖音 H5 应用配置不完整, 请先在「三方平台管理」中配置
            throw new BizInfoException(CommonErrorCode.SYSTEM_ERROR, "error.social.douyinH5NotConfigured");
        }
        DouyinAuthResult data = douyinH5AuthService.getOpenIdByCode(
                config.getClientKey(), config.getClientSecret(), param.getAuthCode());
        if (StrUtil.isBlank(data.getOpenId())) {
            // 抖音: 获取用户标识失败
            throw new BizInfoException(CommonErrorCode.SYSTEM_ERROR, "error.douyin.authFailed", "openId is blank");
        }
        AuthResult authResult = new AuthResult()
                .setOpenId(data.getOpenId())
                .setAccessToken(data.getAccessToken())
                .setStatus(ChannelAuthStatusEnum.SUCCESS.getCode());
        authSessionStore.writeResultByQueryCode(param.getQueryCode(), session, authResult);
        return authResult;
    }

    /// 抖音 H5 应用配置是否完整(clientKey/clientSecret 均非空)
    private boolean isDouyinH5Configured(PlatformDouyinH5AuthConfig config) {
        return StrUtil.isNotBlank(config.getClientKey()) && StrUtil.isNotBlank(config.getClientSecret());
    }
}
