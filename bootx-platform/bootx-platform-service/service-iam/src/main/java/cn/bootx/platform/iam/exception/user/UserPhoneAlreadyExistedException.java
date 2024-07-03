package cn.bootx.platform.iam.exception.user;

import cn.bootx.platform.core.exception.BizException;

import static cn.bootx.platform.iam.code.IamErrorCode.USER_PHONE_ALREADY_EXISTED;

/**
 * 用户手机已存在异常
 *
 * @author xxm
 * @since 2020/5/7 18:25
 */
public class UserPhoneAlreadyExistedException extends BizException {

    public UserPhoneAlreadyExistedException() {
        super(USER_PHONE_ALREADY_EXISTED, "用户手机已存在");
    }

}
