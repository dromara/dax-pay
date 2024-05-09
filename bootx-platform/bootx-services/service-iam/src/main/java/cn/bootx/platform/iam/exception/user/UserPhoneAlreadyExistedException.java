package cn.bootx.platform.iam.exception.user;

import cn.bootx.platform.common.core.exception.BizException;

import java.io.Serializable;

import static cn.bootx.platform.iam.code.IamErrorCode.USER_PHONE_ALREADY_EXISTED;

/**
 * 用户手机已存在异常
 *
 * @author xxm
 * @since 2020/5/7 18:25
 */
public class UserPhoneAlreadyExistedException extends BizException implements Serializable {

    private static final long serialVersionUID = -8925952529440870552L;

    public UserPhoneAlreadyExistedException() {
        super(USER_PHONE_ALREADY_EXISTED, "用户手机已存在");
    }

}
