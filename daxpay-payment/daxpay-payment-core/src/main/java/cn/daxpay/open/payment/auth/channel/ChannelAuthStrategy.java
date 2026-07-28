package cn.daxpay.open.payment.auth.channel;

import cn.daxpay.open.payment.auth.core.AuthSession;
import cn.daxpay.open.payment.unipay.param.assist.AuthCodeParam;
import cn.daxpay.open.payment.unipay.param.assist.GenerateAuthUrlParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.daxpay.open.platform.core.enums.unipay.ChannelAuthTypeEnum;

/// # 通道认证策略（按 authType 路由，不耦合支付产品）
///
/// 认证（取 openId/userId）与支付产品（直连/服务商/三方聚合）正交。
/// 每种认证类型（微信/抖音）只需一个策略实现，不按 ProductEnum 拆分。
public interface ChannelAuthStrategy {

    /// 认证类型（路由键）
    /// @see ChannelAuthTypeEnum
    ChannelAuthTypeEnum getAuthType();

    /// 获取授权链接
    ///
    /// @param authToken 认证会话码, 由上层 [ProductAuthService] 生成注入,
    ///                  策略负责将其拼入回调地址; 授权回跳时凭此恢复上下文。
    /// @param session 认证会话(可变对象), 策略负责写入回调恢复所需的应用引用
    ///                (微信写 wxAppScope/wxAppRefId; 抖音不写, 用 channelMchNo)
    AuthUrlResult generateAuthUrl(GenerateAuthUrlParam param, String authToken, AuthSession session);

    /// 通过AuthCode获取认证结果
    ///
    /// @param session 认证会话(由 ProductAuthService.auth 从 Redis 加载后注入),
    ///                策略据此恢复应用凭证; 小程序直连场景可为 null。
    AuthResult doAuth(AuthCodeParam param, AuthSession session);
}