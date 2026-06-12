package org.dromara.daxpay.platform.capability.auth.exception;

import org.dromara.daxpay.platform.core.exception.BizException;

import static org.dromara.daxpay.platform.core.code.CommonErrorCode.AUTHENTICATION_FAIL;

/// # 未登录异常
///
public class NotLoginException extends BizException {

    public NotLoginException(String msg) {
        super(AUTHENTICATION_FAIL, msg);
    }

    public NotLoginException() {
        super(AUTHENTICATION_FAIL, "用户未登录");
        initMessageKey("error.auth.auth.notLogin");
    }


    public NotLoginException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }
}
