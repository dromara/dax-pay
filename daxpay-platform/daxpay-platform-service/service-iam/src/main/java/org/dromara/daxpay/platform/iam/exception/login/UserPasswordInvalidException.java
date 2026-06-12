package org.dromara.daxpay.platform.iam.exception.login;

import org.dromara.daxpay.platform.core.exception.BizException;

import static org.dromara.daxpay.platform.iam.code.IamErrorCode.USER_PASSWORD_INVALID;

/// # 用户密码不正确异常
///
public class UserPasswordInvalidException extends BizException {

    public UserPasswordInvalidException() {
        // 用户密码不正确
        super(USER_PASSWORD_INVALID, "用户密码不正确");
        initMessageKey("error.iam.password.invalid");
    }

}
