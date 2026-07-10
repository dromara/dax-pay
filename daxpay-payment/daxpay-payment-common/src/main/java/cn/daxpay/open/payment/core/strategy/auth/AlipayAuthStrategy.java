package cn.daxpay.open.payment.core.strategy.auth;

import cn.daxpay.open.platform.capability.alipay.auth.config.AlipayAuthConfig;
import cn.daxpay.open.platform.capability.alipay.auth.result.AlipayAuthResult;
import cn.daxpay.open.platform.capability.alipay.auth.service.AlipayAuthCapability;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.system.service.config.auth.PlatformAlipayAuthConfigService;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.daxpay.open.payment.core.assist.AuthSession;
import cn.daxpay.open.payment.unipay.param.assist.AuthCodeParam;
import cn.daxpay.open.payment.unipay.param.assist.GenerateAuthUrlParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 支付宝通道认证策略
///
/// 支付宝直连模式(ALIPAY)下获取用户标识(userId)。与微信策略不同, 支付宝认证不依赖商户级配置,
/// 统一使用**平台级**支付宝配置([PlatformAlipayAuthConfigService]), 调用 `alipay.system.oauth.token` 换 userId。
///
/// ## 适用场景
/// - 支付场景获取支付宝 userId(如 ALIPAY_JSAPI 需要)
/// - 三方登录的支付宝授权(iam 模块 AlipaySocialAuthRequest 复用同一份平台配置)
///
/// ## 回调机制
/// 与微信/抖音策略同构: 回调地址固定为 `{paymentGatewayBaseUrl}/auth/alipay`,
/// 会话标识 authToken 通过 OAuth state 参数透传, 授权回跳时凭 authToken 恢复会话上下文
/// (由 [ChannelAuthService] 管理)。
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayAuthStrategy extends AbsChannelAuthStrategy {

    /// 支付宝通道认证回调路径(支付网关前端路由, 固定路径)
    private static final String AUTH_CALLBACK_PATH = "/auth/alipay";

    /// 授权范围: auth_base(静默授权, 仅取 userId, 不弹确认页, 支付场景体验更好)
    private static final String SCOPE = "auth_base";

    private final PlatformAlipayAuthConfigService platformAlipayAuthConfigService;
    private final PlatformUrlConfigService platformUrlConfigService;
    private final AlipayAuthCapability alipayAuthCapability;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.ALIPAY;
    }

    /// 生成支付宝授权链接
    ///
    /// 拼接固定回调地址(`/auth/alipay`), 用平台级配置调 [AlipayAuthCapability] 生成支付宝授权页 URL。
    /// authToken 通过 OAuth state 透传。
    @Override
    public AuthUrlResult generateAuthUrl(GenerateAuthUrlParam param, String authToken) {
        AlipayAuthConfig capabilityConfig = platformAlipayAuthConfigService.toCapabilityConfig();
        if (!alipayAuthCapability.isConfigured(capabilityConfig)) {
            // 支付宝: 平台级支付宝配置不完整, 请先在「三方平台管理」中配置
            throw new BizInfoException(CommonErrorCode.SYSTEM_ERROR, "error.social.alipayNotConfigured");
        }
        String redirectUri = this.buildRedirectUri();
        // state 用 authToken, 支付宝回调原样回传, H5 从 state 恢复会话
        // 平台级配置固定生产环境, 不再读取 sandbox
        String authUrl = alipayAuthCapability.generateAuthUrl(capabilityConfig, redirectUri, SCOPE, authToken, false);
        return new AuthUrlResult().setAuthUrl(authUrl);
    }

    /// 通过授权 authCode 换取支付宝 userId
    ///
    /// 支付宝返回的 userId 统一映射到 [AuthResult] 的 openId 字段(支付链路按 openId 取值)。
    @Override
    public AuthResult doAuth(AuthCodeParam param, AuthSession session) {
        AlipayAuthConfig capabilityConfig = platformAlipayAuthConfigService.toCapabilityConfig();
        AlipayAuthResult authResult = alipayAuthCapability.getUserId(capabilityConfig, param.getAuthCode());
        if (StrUtil.isBlank(authResult.getUserId())) {
            // 支付宝: 获取用户标识失败
            throw new BizInfoException(CommonErrorCode.SYSTEM_ERROR, "error.alipay.authFailed", "userId is blank");
        }
        // 支付宝 userId 映射到 openId(支付链路统一用 openId 字段)
        return new AuthResult()
                .setOpenId(authResult.getUserId())
                .setUserId(authResult.getUserId());
    }

    /// 拼接认证回调地址: {paymentGatewayBaseUrl}/auth/alipay(固定路径)
    private String buildRedirectUri() {
        String base = platformUrlConfigService.getUrlConfig().getPaymentGatewayBaseUrl();
        if (StrUtil.isBlank(base)) {
            // 支付网关前端地址未配置
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.gatewayUrlNotConfigured");
        }
        return StrUtil.removeSuffix(base, "/") + AUTH_CALLBACK_PATH;
    }
}
