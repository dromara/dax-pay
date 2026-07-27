package cn.daxpay.open.payment.auth.platform;

import cn.daxpay.open.payment.unipay.param.assist.AuthCodeParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.daxpay.open.platform.capability.alipay.auth.config.AlipayAuthConfig;
import cn.daxpay.open.platform.capability.alipay.auth.result.AlipayAuthResult;
import cn.daxpay.open.platform.capability.alipay.auth.service.AlipayAuthCapability;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.unipay.ChannelAuthStatusEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.system.service.config.auth.PlatformAlipayAuthConfigService;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import cn.daxpay.open.payment.auth.core.AuthScene;
import cn.daxpay.open.payment.auth.core.AuthSession;
import cn.daxpay.open.payment.auth.core.AuthSessionStore;
import cn.daxpay.open.payment.auth.core.AuthRedirectUri;
import cn.daxpay.open.payment.auth.merchant.ChannelAuthService;

/// # 支付宝平台级认证 Provider
///
/// 会话标记 `source=platform_alipay`, 认证分发层 [ChannelAuthService] 据 source 查找本 Provider。
@Slf4j
@Component
@RequiredArgsConstructor
public class AlipayAuthProvider implements PlatformAuthProvider {

    /// 支付宝授权范围: auth_base(静默授权, 仅取 userId, 不弹确认页)
    private static final String ALIPAY_SCOPE = "auth_base";

    private final AuthSessionStore authSessionStore;
    private final PlatformAlipayAuthConfigService platformAlipayAuthConfigService;
    private final PlatformUrlConfigService platformUrlConfigService;
    private final AlipayAuthCapability alipayAuthCapability;

    @Override
    public String sourceCode() {
        return AuthSession.SOURCE_PLATFORM_ALIPAY;
    }

    /// 生成支付宝授权链接
    ///
    /// 配置校验前置, 再建 session/queryCode 落 Redis, 最后拼装授权 URL。
    @Override
    public AuthUrlResult generateAuthUrl(String returnPath) {
        // 配置校验前置, 避免配置缺失时 session 残留至 TTL
        AlipayAuthConfig config = loadConfigOrThrow();
        String authToken = IdUtil.fastSimpleUUID();
        String queryCode = RandomUtil.randomString(10);
        AuthSession session = new AuthSession()
                .setSource(AuthSession.SOURCE_PLATFORM_ALIPAY)
                .setQueryCode(queryCode)
                .setReturnPath(returnPath)
                .setScene(AuthScene.PLATFORM.getCode());
        authSessionStore.saveSession(authToken, session);
        // redirect_uri 为固定路径(见 [AuthRedirectUri]), authToken 通过 OAuth state 透传
        String redirectUri = AuthRedirectUri.ALIPAY.buildRedirectUri(platformUrlConfigService);
        String authUrl = alipayAuthCapability.generateAuthUrl(config, redirectUri, ALIPAY_SCOPE, authToken, false);
        authSessionStore.saveWaitingResult(queryCode);
        return new AuthUrlResult().setAuthUrl(authUrl).setQueryCode(queryCode).setAuthToken(authToken);
    }

    /// 通过 authCode 换 userId/openId(平台级配置)
    ///
    /// 统一映射: 支付链路按 openId 取值, 同时回填 userId; 结果落库供 queryCode 轮询。
    @Override
    public AuthResult auth(AuthCodeParam param, AuthSession session) {
        AlipayAuthConfig config = loadConfigOrThrow();
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
        fillReturnPath(authResult, session);
        authSessionStore.writeResultByQueryCode(param.getQueryCode(), session, authResult);
        return authResult;
    }

    /// 加载支付宝配置并校验完整性
    private AlipayAuthConfig loadConfigOrThrow() {
        AlipayAuthConfig config = platformAlipayAuthConfigService.toCapabilityConfig();
        if (!alipayAuthCapability.isConfigured(config)) {
            // 支付宝: 平台级支付宝配置不完整, 请先在「三方平台管理」中配置
            throw new BizInfoException(CommonErrorCode.SYSTEM_ERROR, "error.social.alipayNotConfigured");
        }
        return config;
    }
}
