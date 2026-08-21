package cn.daxpay.open.platform.iam.exception.auth;

import cn.daxpay.open.platform.core.exception.BizInfoException;

/// # 初始密码访问受限异常
///
/// 区别于密码过期(40301): 用户密码为管理员代设的初始密码, 首次登录后必须修改, 期间仅放行改密等白名单路径。
public class InitialPasswordAccessException extends BizInfoException {

    public InitialPasswordAccessException() {
        super(40302, "error.auth.initialPasswordChangeRequired");
    }

    public InitialPasswordAccessException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }
}
