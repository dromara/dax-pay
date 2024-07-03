package cn.bootx.platform.iam.exception.user;

import cn.bootx.platform.core.exception.BizException;

import static cn.bootx.platform.iam.code.IamErrorCode.NONE_PHONE_AND_EMAIL;

/**
 * 用户手机号和邮箱不可都为空的异常
 *
 * @author xxm
 * @since 2020/5/7 18:30
 */
public class UserNonePhoneAndEmailException extends BizException {

    public UserNonePhoneAndEmailException() {
        super(NONE_PHONE_AND_EMAIL, "用户的电话和电子邮件必须包含一个");
    }

}
