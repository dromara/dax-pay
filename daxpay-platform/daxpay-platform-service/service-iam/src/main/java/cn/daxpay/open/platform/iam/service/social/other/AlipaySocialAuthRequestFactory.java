package cn.daxpay.open.platform.iam.service.social.other;

import cn.daxpay.open.platform.capability.alipay.auth.service.AlipayAuthCapability;
import cn.daxpay.open.platform.capability.social.justauth.SocialAuthConfig;
import cn.daxpay.open.platform.capability.social.justauth.request.SocialAuthRequest;
import cn.daxpay.open.platform.iam.service.social.SocialLoginService;
import cn.daxpay.open.platform.system.service.config.PlatformAlipayAuthConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/// # 支付宝社交授权请求工厂
///
/// 在 iam 模块创建 [AlipaySocialAuthRequest], 避免 capability-social 反向依赖 capability-alipay。
/// 由 [SocialLoginService] 在 source=alipay 时委托本工厂, 其余平台仍走 [SocialAuthRequestFactory]。
///
@Component
@RequiredArgsConstructor
public class AlipaySocialAuthRequestFactory {

    private final AlipayAuthCapability alipayAuthCapability;

    private final PlatformAlipayAuthConfigService platformAlipayAuthConfigService;

    /// 创建支付宝授权请求(凭据在请求内部从平台配置读取, config 仅提供 redirectUri)
    public SocialAuthRequest create(SocialAuthConfig config) {
        return new AlipaySocialAuthRequest(config, alipayAuthCapability, platformAlipayAuthConfigService);
    }
}
