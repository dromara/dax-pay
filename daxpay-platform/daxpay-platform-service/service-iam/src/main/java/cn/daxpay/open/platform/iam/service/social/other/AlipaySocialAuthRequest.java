package cn.daxpay.open.platform.iam.service.social.other;

import cn.daxpay.open.platform.capability.alipay.auth.config.AlipayAuthConfig;
import cn.daxpay.open.platform.capability.alipay.auth.result.AlipayAuthResult;
import cn.daxpay.open.platform.capability.alipay.auth.service.AlipayAuthCapability;
import cn.daxpay.open.platform.capability.social.justauth.SocialAuthConfig;
import cn.daxpay.open.platform.capability.social.justauth.SocialSourceEnum;
import cn.daxpay.open.platform.capability.social.justauth.exception.SocialException;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthCallback;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthToken;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthUser;
import cn.daxpay.open.platform.capability.social.justauth.request.SocialAuthRequest;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.iam.service.social.SocialLoginService;
import cn.daxpay.open.platform.system.service.config.PlatformAlipayAuthConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;

/// # 支付宝社交授权请求
///
/// 实现 [SocialAuthRequest], 将支付宝非标准 OAuth(`alipay.system.oauth.token`)
/// 收口进统一的 Social 编排([SocialLoginService]), 对外与 JustAuth 平台同入口。
/// 凭据来自平台级 [PlatformAlipayAuthConfigService], 不使用 [SocialAuthConfig] 的 clientId/secret;
/// 公钥/证书模式差异封装在 [AlipayAuthCapability] 内, 本类不分支 authType。
///
@RequiredArgsConstructor
public class AlipaySocialAuthRequest implements SocialAuthRequest {

    /// 授权范围: auth_user(需用户确认, 可获取用户信息)
    private static final String SCOPE = "auth_user";

    private final SocialAuthConfig config;

    private final AlipayAuthCapability alipayAuthCapability;

    private final PlatformAlipayAuthConfigService platformAlipayAuthConfigService;

    /// 生成支付宝授权地址(仅用平台配置 appId + redirectUri + state)
    @Override
    public String authorize(String state) {
        AlipayAuthConfig capabilityConfig = this.requireCapabilityConfig();
        // redirectUri 与标准平台一致: 基础 path + /{source}
        String redirectUri = this.buildRedirectUri();
        // 平台级配置固定生产环境
        return alipayAuthCapability.generateAuthUrl(capabilityConfig, redirectUri, SCOPE, state, false);
    }

    /// 用授权码换 AuthUser(前端已将 auth_code 归一为 code)
    @Override
    public AuthUser login(AuthCallback callback) {
        if (StrUtil.isBlank(callback.getCode())) {
            // 授权码不能为空
            throw new SocialException("error.social.authCodeBlank");
        }
        AlipayAuthConfig capabilityConfig = this.requireCapabilityConfig();
        AlipayAuthResult authResult = alipayAuthCapability.getUserId(capabilityConfig, callback.getCode());
        String alipayUserId = authResult.getUserId();
        if (StrUtil.isBlank(alipayUserId)) {
            throw new SocialException("error.social.oauthFailed");
        }
        AuthToken token = new AuthToken()
                .setAccessToken(authResult.getAccessToken())
                .setOpenId(authResult.getOpenId())
                .setUnionId(alipayUserId);
        return new AuthUser()
                .setSource(SocialSourceEnum.ALIPAY.getCode())
                .setUuid(alipayUserId)
                .setNickname("支付宝用户")
                .setToken(token);
    }

    /// 支付宝换用户在 [login] 内一次完成, 不单独暴露 accessToken 步骤
    @Override
    public AuthToken getAccessToken(AuthCallback callback) {
        throw new SocialException("error.social.unsupportedSource");
    }

    /// 支付宝换用户在 [login] 内一次完成, 不单独暴露 userInfo 步骤
    @Override
    public AuthUser getUserInfo(AuthToken token) {
        throw new SocialException("error.social.unsupportedSource");
    }

    /// 加载并校验平台级支付宝配置完整可用
    private AlipayAuthConfig requireCapabilityConfig() {
        AlipayAuthConfig capabilityConfig = platformAlipayAuthConfigService.toCapabilityConfig();
        if (!alipayAuthCapability.isConfigured(capabilityConfig)) {
            // 支付宝: 平台级支付宝配置不完整, 请先在「三方平台管理」中配置
            throw new OperationFailException("error.social.alipayNotConfigured");
        }
        return capabilityConfig;
    }

    /// 构建回调地址(配置基础路径 + 平台编码), 与 [AbstractSocialAuthRequest#buildRedirectUri] 对齐
    private String buildRedirectUri() {
        String base = config.getRedirectUri();
        if (base == null) {
            base = "";
        }
        if (base.endsWith("/")) {
            base = base.substring(0, base.length() - 1);
        }
        return base + "/" + SocialSourceEnum.ALIPAY.getCode();
    }
}
