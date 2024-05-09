package cn.bootx.platform.iam.exception.user;

import cn.bootx.platform.common.core.exception.FatalException;

import static cn.bootx.platform.iam.code.IamErrorCode.USER_INFO_NOT_EXISTS;

/**
 * 用户信息不存在异常
 *
 * @author xxm
 * @since 2020/5/7 18:29
 */
public class UserInfoNotExistsException extends FatalException {

    public UserInfoNotExistsException() {
        super(USER_INFO_NOT_EXISTS, "用户信息不存在");
    }

}
