package cn.daxpay.open.payment.strategy.auth;

import cn.daxpay.open.payment.auth.AuthSession;
import cn.daxpay.open.payment.strategy.PaymentStrategy;
import cn.daxpay.open.payment.unipay.param.assist.AuthCodeParam;
import cn.daxpay.open.payment.unipay.param.assist.GenerateAuthUrlParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;

/// # 通道抽象认证策略
///
/// 负责获取支付所需的用户标识(微信 openId / 支付宝 userId)。按支付产品([cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum])
/// 注册, 与支付策略粒度一致。策略复用支付的商户配置体系定位通道应用(appId/appSecret)。
public abstract class AbsChannelAuthStrategy implements PaymentStrategy {

    /// 获取授权链接
    ///
    /// @param authToken 认证会话码, 由上层 [ChannelAuthService] 生成注入,
    ///                  策略负责将其拼入回调地址; 授权回跳时凭此恢复上下文。
    public abstract AuthUrlResult generateAuthUrl(GenerateAuthUrlParam param, String authToken);

    /// 通过AuthCode获取认证结果
    ///
    /// @param session 认证会话上下文(H5场景从 authToken 恢复; 小程序直连场景可为空, 此时从 param 取上下文)。
    ///                策略需兼容 session 为 null 的情况(优先用 param 的 channelMchNo/capability/channelAppId)。
    public abstract AuthResult doAuth(AuthCodeParam param, AuthSession session);

}
