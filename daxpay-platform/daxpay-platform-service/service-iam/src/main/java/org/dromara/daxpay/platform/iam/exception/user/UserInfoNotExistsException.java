package org.dromara.daxpay.platform.iam.exception.user;

import org.dromara.daxpay.platform.core.exception.BizException;

import static org.dromara.daxpay.platform.iam.code.IamErrorCode.USER_INFO_NOT_EXISTS;

/// # 用户信息不存在异常
///
public class UserInfoNotExistsException extends BizException {

    public UserInfoNotExistsException() {
        super(USER_INFO_NOT_EXISTS, "用户信息不存在");
        initMessageKey("error.iam.user.userInfoNotExists");
    }


    public UserInfoNotExistsException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }
}
