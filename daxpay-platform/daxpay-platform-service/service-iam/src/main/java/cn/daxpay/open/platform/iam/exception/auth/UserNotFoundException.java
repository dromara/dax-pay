package cn.daxpay.open.platform.iam.exception.auth;

import cn.daxpay.open.platform.capability.auth.exception.LoginFailureException;

/// # 用户未找到异常
///
public class UserNotFoundException extends LoginFailureException {

    public UserNotFoundException(String account) {
        super(account, "用户未找到");
        initMessageKey("error.auth.userNotFound");
    }

    public UserNotFoundException() {
        super("用户未找到");
        initMessageKey("error.auth.userNotFound");
    }


    public UserNotFoundException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }
}
