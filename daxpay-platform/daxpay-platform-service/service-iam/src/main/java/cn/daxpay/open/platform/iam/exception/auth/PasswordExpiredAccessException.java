package cn.daxpay.open.platform.iam.exception.auth;

import cn.daxpay.open.platform.core.exception.BizInfoException;

/// # 密码过期访问受限异常
///
public class PasswordExpiredAccessException extends BizInfoException {

    public PasswordExpiredAccessException() {
        super(40301, "error.auth.passwordExpired");
    }

    public PasswordExpiredAccessException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }
}
