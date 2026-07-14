package cn.daxpay.open.platform.iam.exception.user;

import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.iam.code.IamErrorCode;

/// # 用户手机已存在异常
///
public class UserPhoneAlreadyExistedException extends BizException {

    public UserPhoneAlreadyExistedException() {
        super(IamErrorCode.USER_PHONE_ALREADY_EXISTED, "用户手机已存在");
        initMessageKey("error.iam.user.userPhoneAlreadyExisted");
    }

    public UserPhoneAlreadyExistedException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }
}
