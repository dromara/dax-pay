package cn.daxpay.open.platform.capability.auth.authentication;

import cn.daxpay.open.platform.capability.auth.entity.AuthInfoResult;
import cn.daxpay.open.platform.capability.auth.entity.LoginAuthContext;

/// # 认证后挑战 SPI
///
/// 认证(凭证校验 + 用户状态校验)通过后、建立会话前, 判断是否需要额外挑战(如双因素认证 / 设备验证 / 风控)。
/// 需要挑战时由 [#createChallenge] 颁发挑战凭证并返回挑战异常, 由全局处理器返回前端。
/// 挑战不属于登录失败, 不触发失败计数与失败日志。
///
public interface PostAuthenticationChallenge {

    /// 是否需要挑战
    /// @param context        认证上下文
    /// @param authInfoResult 认证结果
    boolean required(LoginAuthContext context, AuthInfoResult authInfoResult);

    /// 颁发挑战凭证并返回挑战异常
    /// @return 挑战异常, 由调用方抛出
    AuthenticationChallengeException createChallenge(LoginAuthContext context, AuthInfoResult authInfoResult);
}
