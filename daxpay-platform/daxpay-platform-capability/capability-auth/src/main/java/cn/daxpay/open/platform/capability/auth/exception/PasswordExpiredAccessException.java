package cn.daxpay.open.platform.capability.auth.exception;

import cn.daxpay.open.platform.core.exception.BizInfoException;

/// # 密码过期访问受限异常
///
public class PasswordExpiredAccessException extends BizInfoException {

    public PasswordExpiredAccessException() {
        super(40301, "密码已过期，请先修改密码");
        initMessageKey("error.auth.auth.passwordExpired");
    }

    public PasswordExpiredAccessException(String message) {
        super(40301, message);
    }

    public PasswordExpiredAccessException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }
}
