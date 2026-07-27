package cn.daxpay.open.payment.auth.platform;

import cn.daxpay.open.payment.unipay.param.assist.AuthCodeParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.daxpay.open.platform.capability.douyin.auth.result.DouyinAuthResult;
import cn.daxpay.open.platform.capability.douyin.auth.service.DouyinH5AuthService;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.unipay.ChannelAuthStatusEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.system.entity.config.platform.auth.PlatformDouyinH5AuthConfig;
import cn.daxpay.open.platform.system.service.config.auth.PlatformDouyinH5AuthConfigService;
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

/// # 抖音 H5 平台级认证 Provider
///
/// 会话标记 `source=platform_douyin`, 仅调试场景使用。
/// 抖音 silent_auth 要求 redirect_uri 与平台配置完全一致。
@Slf4j
@Component
@RequiredArgsConstructor
public class DouyinH5AuthProvider implements PlatformAuthProvider {

    private final AuthSessionStore authSessionStore;
    private final PlatformDouyinH5AuthConfigService platformDouyinH5AuthConfigService;
    private final PlatformUrlConfigService platformUrlConfigService;
    private final DouyinH5AuthService douyinH5AuthService;

    @Override
    public String sourceCode() {
        return AuthSession.SOURCE_PLATFORM_DOUYIN;
    }

    @Override
    public AuthUrlResult generateAuthUrl(String returnPath) {
        PlatformDouyinH5AuthConfig config = loadConfigOrThrow();
        String authToken = IdUtil.fastSimpleUUID();
        String queryCode = RandomUtil.randomString(10);
        AuthSession session = new AuthSession()
                .setSource(AuthSession.SOURCE_PLATFORM_DOUYIN)
                .setQueryCode(queryCode)
                .setReturnPath(returnPath)
                .setScene(AuthScene.PLATFORM.getCode());
        authSessionStore.saveSession(authToken, session);
        // redirect_uri 为固定路径(见 [AuthRedirectUri], 抖音要求与平台配置完全一致), authToken 通过 state 透传
        String redirectUri = AuthRedirectUri.DOUYIN.buildRedirectUri(platformUrlConfigService);
        String authUrl = douyinH5AuthService.buildSilentAuthUrl(config.getClientKey(), redirectUri, authToken);
        authSessionStore.saveWaitingResult(queryCode);
        return new AuthUrlResult().setAuthUrl(authUrl).setQueryCode(queryCode).setAuthToken(authToken);
    }

    @Override
    public AuthResult auth(AuthCodeParam param, AuthSession session) {
        PlatformDouyinH5AuthConfig config = loadConfigOrThrow();
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
        fillReturnPath(authResult, session);
        authSessionStore.writeResultByQueryCode(param.getQueryCode(), session, authResult);
        return authResult;
    }

    private PlatformDouyinH5AuthConfig loadConfigOrThrow() {
        PlatformDouyinH5AuthConfig config = platformDouyinH5AuthConfigService.getDouyinH5AuthConfig();
        if (!isConfigured(config)) {
            // 抖音: 平台级抖音 H5 应用配置不完整, 请先在「三方平台管理」中配置
            throw new BizInfoException(CommonErrorCode.SYSTEM_ERROR, "error.social.douyinH5NotConfigured");
        }
        return config;
    }

    private boolean isConfigured(PlatformDouyinH5AuthConfig config) {
        return StrUtil.isNotBlank(config.getClientKey()) && StrUtil.isNotBlank(config.getClientSecret());
    }
}
