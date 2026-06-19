package cn.daxpay.open.platform.iam.exception.user;

import cn.daxpay.open.platform.core.exception.BizException;

import static cn.daxpay.open.platform.iam.code.IamErrorCode.NONE_PHONE_AND_EMAIL;

/// # 用户手机号和邮箱不可都为空的异常
///
public class UserNonePhoneAndEmailException extends BizException {

    public UserNonePhoneAndEmailException() {
        super(NONE_PHONE_AND_EMAIL, "用户的电话和电子邮件必须包含一个");
        initMessageKey("error.iam.user.userNonePhoneAndEmail");
    }


    public UserNonePhoneAndEmailException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }
}
