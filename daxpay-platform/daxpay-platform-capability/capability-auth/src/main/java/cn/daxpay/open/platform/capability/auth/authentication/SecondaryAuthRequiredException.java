package cn.daxpay.open.platform.capability.auth.authentication;

import cn.daxpay.open.platform.capability.auth.exception.LoginFailureException;

/// # 需二次验证异常基类
///
/// 认证已通过, 但仍需额外检查(如双因素)时抛出。
/// 继承 [LoginFailureException] 仅复用 userId / account 字段, 语义上**不是**登录失败,
/// 不应触发失败计数与失败日志。
///
public class SecondaryAuthRequiredException extends LoginFailureException {

    public SecondaryAuthRequiredException(Long userId, String account, String messageKey) {
        super(userId, account, messageKey);
    }
}
