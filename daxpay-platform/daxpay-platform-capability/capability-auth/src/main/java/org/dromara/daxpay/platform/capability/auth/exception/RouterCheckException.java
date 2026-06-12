package org.dromara.daxpay.platform.capability.auth.exception;

import org.dromara.daxpay.platform.core.exception.BizInfoException;

/// # 路径检查异常
///
public class RouterCheckException extends BizInfoException {

    public RouterCheckException() {
        super("没有对应请求路径的权限");
    }
    public RouterCheckException(String message) {
        super(message);
    }


    public RouterCheckException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }
}
