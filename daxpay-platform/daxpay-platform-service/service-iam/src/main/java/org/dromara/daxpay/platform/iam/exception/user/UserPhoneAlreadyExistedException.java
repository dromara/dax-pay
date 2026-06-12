package org.dromara.daxpay.platform.iam.exception.user;

import org.dromara.daxpay.platform.core.exception.BizException;

import static org.dromara.daxpay.platform.iam.code.IamErrorCode.USER_PHONE_ALREADY_EXISTED;

/// # 用户手机已存在异常
///
public class UserPhoneAlreadyExistedException extends BizException {

    public UserPhoneAlreadyExistedException() {
        super(USER_PHONE_ALREADY_EXISTED, "用户手机已存在");
        initMessageKey("error.iam.user.userPhoneAlreadyExisted");
    }


    public UserPhoneAlreadyExistedException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }
}
