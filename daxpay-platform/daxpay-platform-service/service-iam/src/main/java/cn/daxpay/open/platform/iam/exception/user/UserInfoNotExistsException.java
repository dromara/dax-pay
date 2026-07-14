package cn.daxpay.open.platform.iam.exception.user;

import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.iam.code.IamErrorCode;

/// # 用户信息不存在异常
///
public class UserInfoNotExistsException extends BizException {

    public UserInfoNotExistsException() {
        super(IamErrorCode.USER_INFO_NOT_EXISTS, "用户信息不存在");
        initMessageKey("error.iam.user.userInfoNotExists");
    }

    public UserInfoNotExistsException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }
}
