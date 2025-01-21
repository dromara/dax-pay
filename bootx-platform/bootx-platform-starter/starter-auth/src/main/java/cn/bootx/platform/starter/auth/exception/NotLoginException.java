package cn.bootx.platform.starter.auth.exception;

import cn.bootx.platform.core.exception.BizInfoException;

import static cn.bootx.platform.core.code.CommonErrorCode.AUTHENTICATION_FAIL;

/**
 * 未登录异常
 *
 * @author xxm
 * @since 2021/12/22
 */
public class NotLoginException extends BizInfoException {

    public NotLoginException(String msg) {
        super(AUTHENTICATION_FAIL, msg);
    }

    public NotLoginException() {
        super(AUTHENTICATION_FAIL, "未登录");
    }

}
