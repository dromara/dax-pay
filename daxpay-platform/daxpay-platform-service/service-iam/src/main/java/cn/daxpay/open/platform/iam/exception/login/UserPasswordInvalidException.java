package cn.daxpay.open.platform.iam.exception.login;

import cn.daxpay.open.platform.core.exception.BizException;

import static cn.daxpay.open.platform.iam.code.IamErrorCode.USER_PASSWORD_INVALID;

/// # 用户密码不正确异常
///
public class UserPasswordInvalidException extends BizException {

    public UserPasswordInvalidException() {
        // 用户密码不正确
        super(USER_PASSWORD_INVALID, "用户密码不正确");
        initMessageKey("error.iam.password.invalid");
    }

}
