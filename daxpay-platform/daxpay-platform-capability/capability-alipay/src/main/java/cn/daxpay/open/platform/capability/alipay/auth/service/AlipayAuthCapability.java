package cn.daxpay.open.platform.capability.alipay.auth.service;

import cn.daxpay.open.platform.capability.alipay.auth.config.AlipayAuthConfig;
import cn.daxpay.open.platform.capability.alipay.auth.config.AlipayAuthTypeEnum;
import cn.daxpay.open.platform.capability.alipay.auth.result.AlipayAuthResult;
import cn.daxpay.open.platform.capability.alipay.openapi.AlipayOpenApiClient;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/// # 支付宝开放平台认证能力
///
/// 封装支付宝 OAuth2 相关调用, 不耦合配置存储。调用方(iam 授权登录端点 / payment 通道认证策略)
/// 从各自配置来源装载 [AlipayAuthConfig] 后传入本服务。
///
/// ## 能力边界
/// - 生成授权链接(纯字符串拼接)
/// - authCode 换 userId/openId/accessToken(直连 `alipay.system.oauth.token`, 无官方 SDK)
/// - 不做 state/queryCode 缓存(由调用方自行管理 Redis 会话)
///
@Slf4j
@Service
public class AlipayAuthCapability {

    /// 支付宝开放平台授权页地址(生产环境)
    private static final String AUTH_URL_PRODUCTION = "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm";

    /// 支付宝开放平台授权页地址(沙箱环境)
    private static final String AUTH_URL_SANDBOX = "https://openauth.alipaydev.com/oauth2/publicAppAuthorize.htm";

    /// 生成支付宝授权链接
    ///
    /// @param config      支付宝开放平台配置(只用 appId)
    /// @param redirectUri 回调地址(需在支付宝开放平台配置过)
    /// @param scope       授权范围: `auth_user`(需用户确认, 可获取昵称头像) / `auth_base`(静默授权, 仅 userId)
    /// @param state       OAuth2 state(由调用方生成, 用于 CSRF 防护与上下文关联)
    /// @param sandbox     是否沙箱环境
    public String generateAuthUrl(AlipayAuthConfig config, String redirectUri, String scope, String state, boolean sandbox) {
        String baseUrl = sandbox ? AUTH_URL_SANDBOX : AUTH_URL_PRODUCTION;
        String encodedRedirect = URLEncoder.encode(redirectUri, StandardCharsets.UTF_8);
        // 支付宝要求 app_id + scope + redirect_uri 三个参数, state 可选
        StringBuilder url = new StringBuilder(baseUrl)
                .append("?app_id=").append(config.getAppId())
                .append("&scope=").append(scope)
                .append("&redirect_uri=").append(encodedRedirect);
        if (Objects.toString(state, "").isBlank() == false) {
            url.append("&state=").append(state);
        }
        return url.toString();
    }

    /// 通过 authCode 换取用户标识
    ///
    /// 直连支付宝 `alipay.system.oauth.token`(grant_type=authorization_code),
    /// 返回 userId(传统) / openId(新标准) / accessToken(可用于后续 user.info.share)。
    /// 平台级配置固定生产网关。
    ///
    /// @param config   支付宝开放平台配置(完整凭据)
    /// @param authCode 支付宝回调回传的 auth_code
    /// @return 含 userId / openId / accessToken 的授权结果
    public AlipayAuthResult getUserId(AlipayAuthConfig config, String authCode) {
        try {
            return AlipayOpenApiClient.oauthToken(config, authCode, false);
        } catch (OperationFailException e) {
            throw e;
        } catch (Exception e) {
            log.error("支付宝换取用户标识异常", e);
            // 支付宝: 换取用户标识失败: {0}
            throw new OperationFailException("error.alipay.authFailed", e.getMessage());
        }
    }

    /// 判断配置是否完整可用(appId + privateKey 必填, 公钥/证书按 authType 二选一)
    public boolean isConfigured(AlipayAuthConfig config) {
        if (config == null) {
            return false;
        }
        if (Objects.toString(config.getAppId(), "").isBlank()
                || Objects.toString(config.getPrivateKey(), "").isBlank()) {
            return false;
        }
        AlipayAuthTypeEnum authType = AlipayAuthTypeEnum.fromCode(config.getAuthType());
        if (authType.isCert()) {
            return !Objects.toString(config.getAppCert(), "").isBlank()
                    && !Objects.toString(config.getAlipayCert(), "").isBlank()
                    && !Objects.toString(config.getAlipayRootCert(), "").isBlank();
        }
        return !Objects.toString(config.getAlipayPublicKey(), "").isBlank();
    }
}
