package cn.daxpay.open.platform.capability.auth.exception;

import cn.daxpay.open.platform.core.exception.BizException;

import static cn.daxpay.open.platform.core.code.CommonErrorCode.AUTHENTICATION_FAIL;

/// # 未登录异常
///
public class NotLoginException extends BizException {

    public NotLoginException(String msg) {
        super(AUTHENTICATION_FAIL, msg);
    }

    public NotLoginException() {
        super(AUTHENTICATION_FAIL, "用户未登录");
        initMessageKey("error.auth.notLogin");
    }


    public NotLoginException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }
}
