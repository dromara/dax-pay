package cn.bootx.platform.iam.exception.login;

import cn.bootx.platform.common.core.exception.BizException;

import java.io.Serializable;

import static cn.bootx.platform.iam.code.IamErrorCode.USER_PASSWORD_INVALID;

/**
 * 用户密码不正确异常
 *
 * @author xxm
 * @since 2020/5/7 18:16
 */
public class UserPasswordInvalidException extends BizException implements Serializable {

    private static final long serialVersionUID = 6321083408077778554L;

    public UserPasswordInvalidException() {
        super(USER_PASSWORD_INVALID, "用户密码不正确");
    }

}
