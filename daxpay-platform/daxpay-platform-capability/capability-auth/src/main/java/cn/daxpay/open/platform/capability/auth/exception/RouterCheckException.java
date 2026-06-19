package cn.daxpay.open.platform.capability.auth.exception;

import cn.daxpay.open.platform.core.exception.BizInfoException;

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
