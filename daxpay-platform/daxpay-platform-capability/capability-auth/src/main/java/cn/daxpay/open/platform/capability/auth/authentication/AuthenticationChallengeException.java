package cn.daxpay.open.platform.capability.auth.authentication;

import cn.daxpay.open.platform.capability.auth.exception.LoginFailureException;

/// # 认证挑战异常基类
///
/// 认证通过后, 但需要额外挑战(如双因素认证)时抛出。
/// 继承 [LoginFailureException] 以复用其 userId / account 字段, 但语义上属"非失败"流程,
/// 不应触发登录失败计数与失败日志。
///
public class AuthenticationChallengeException extends LoginFailureException {

    public AuthenticationChallengeException(Long userId, String account, String messageKey) {
        super(userId, account, messageKey);
    }
}
