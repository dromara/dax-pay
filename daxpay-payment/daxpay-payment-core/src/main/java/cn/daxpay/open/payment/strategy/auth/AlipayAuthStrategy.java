package cn.daxpay.open.payment.strategy.auth;

import cn.daxpay.open.payment.auth.AuthSession;
import cn.daxpay.open.payment.auth.PlatformAuthService;
import cn.daxpay.open.payment.unipay.param.assist.AuthCodeParam;
import cn.daxpay.open.payment.unipay.param.assist.GenerateAuthUrlParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 支付宝通道认证策略
///
/// 支付宝直连模式(ALIPAY)下获取用户标识(userId)。与微信策略不同, 支付宝认证不依赖商户级配置,
/// 统一使用**平台级**支付宝配置, 实现全部委托 [PlatformAuthService], 避免双轨逻辑漂移。
///
/// ## 适用场景
/// - 支付场景获取支付宝 userId(如 ALIPAY_JSAPI 需要)
/// - 经 [ChannelAuthService] 按 product=ALIPAY 路由时的 H5 OAuth / 小程序直连
///
/// ## 回调机制
/// 与微信/抖音策略同构: 回调地址固定为 `{paymentGatewayBaseUrl}/auth/alipay`,
/// 会话标识 authToken 通过 OAuth state 参数透传(会话由 [ChannelAuthService] 管理)。
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayAuthStrategy extends AbsChannelAuthStrategy {

    private final PlatformAuthService platformAuthService;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.ALIPAY;
    }

    /// 生成支付宝授权链接(委托平台服务拼 URL; session/queryCode 已由 ChannelAuthService 创建)
    @Override
    public AuthUrlResult generateAuthUrl(GenerateAuthUrlParam param, String authToken) {
        return new AuthUrlResult().setAuthUrl(platformAuthService.buildAlipayAuthUrl(authToken));
    }

    /// 通过授权 authCode 换取支付宝 userId(委托平台服务, 统一 openId/userId 映射)
    @Override
    public AuthResult doAuth(AuthCodeParam param, AuthSession session) {
        return platformAuthService.authAlipay(param, session);
    }
}
