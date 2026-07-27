package cn.daxpay.open.payment.auth.merchant;

import cn.daxpay.open.payment.auth.core.AuthSession;
import cn.daxpay.open.payment.strategy.PaymentStrategy;
import cn.daxpay.open.payment.unipay.param.assist.AuthCodeParam;
import cn.daxpay.open.payment.unipay.param.assist.GenerateAuthUrlParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.hutool.core.util.StrUtil;

/// # 通道抽象认证策略
///
/// 负责获取支付所需的用户标识(微信 openId / 支付宝 userId)。按支付产品([cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum])
/// 注册, 与支付策略粒度一致。策略复用支付的商户配置体系定位通道应用(appId/appSecret)。
public abstract class AbsProductAuthStrategy implements PaymentStrategy {

    /// 获取授权链接
    ///
    /// @param authToken 认证会话码, 由上层 [ProductAuthService] 生成注入,
    ///                  策略负责将其拼入回调地址; 授权回跳时凭此恢复上下文。
    public abstract AuthUrlResult generateAuthUrl(GenerateAuthUrlParam param, String authToken);

    /// 通过AuthCode获取认证结果
    ///
    /// @param session 认证会话上下文(H5场景从 authToken 恢复; 小程序直连场景可为空, 此时从 param 取上下文)。
    ///                策略需兼容 session 为 null 的情况(优先用 param 的 channelMchNo/capability/channelAppId)。
    public abstract AuthResult doAuth(AuthCodeParam param, AuthSession session);

    /// 解析认证上下文: session 字段优先, param 兜底
    ///
    /// H5 OAuth 重定向场景从 session 恢复(param 仅含 authToken);
    /// 小程序直连场景 session 为空, 从 param 取上下文。
    /// 返回 [ProductAuthContext](channelMchNo / capability / channelAppId)。
    protected ProductAuthContext resolveContext(AuthCodeParam param, AuthSession session) {
        String channelMchNo = session != null && StrUtil.isNotBlank(session.getChannelMchNo())
                ? session.getChannelMchNo() : param.getChannelMchNo();
        String capability = session != null && StrUtil.isNotBlank(session.getCapability())
                ? session.getCapability() : param.getCapability();
        String channelAppId = session != null && StrUtil.isNotBlank(session.getChannelAppId())
                ? session.getChannelAppId() : param.getChannelAppId();
        return new ProductAuthContext(channelMchNo, capability, channelAppId);
    }

}
