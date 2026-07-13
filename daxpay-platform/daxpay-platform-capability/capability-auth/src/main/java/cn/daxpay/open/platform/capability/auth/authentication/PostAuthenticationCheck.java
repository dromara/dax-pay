package cn.daxpay.open.platform.capability.auth.authentication;

import cn.daxpay.open.platform.capability.auth.entity.AuthInfoResult;
import cn.daxpay.open.platform.capability.auth.entity.LoginAuthContext;

/// # 认证通过后的扩展检查
///
/// 凭证校验与用户状态校验通过后、建立登录会话前执行。
/// 若需双因素等补验, 生成预认证令牌并抛出异常, 由全局处理器返回前端;
/// **不算登录失败**, 不触发失败计数与失败日志。
///
public interface PostAuthenticationCheck {

    /// 是否需要二次验证
    /// @param context        认证上下文
    /// @param authInfoResult 认证结果
    boolean required(LoginAuthContext context, AuthInfoResult authInfoResult);

    /// 创建预认证上下文并返回「需二次验证」异常
    /// @return 需二次验证异常, 由调用方抛出
    SecondaryAuthRequiredException createException(LoginAuthContext context, AuthInfoResult authInfoResult);
}
