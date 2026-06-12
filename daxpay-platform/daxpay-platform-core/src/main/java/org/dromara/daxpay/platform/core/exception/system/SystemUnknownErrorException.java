package org.dromara.daxpay.platform.core.exception.system;

import org.dromara.daxpay.platform.core.code.DaxPayErrorCode;
import org.dromara.daxpay.platform.core.exception.PayFailureException;

/// # 未知异常，系统无法处理
///
public class SystemUnknownErrorException extends PayFailureException {

    public SystemUnknownErrorException(String message) {
        super(DaxPayErrorCode.SYSTEM_UNKNOWN_ERROR,message);
    }

    public SystemUnknownErrorException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }

    public SystemUnknownErrorException() {
        // 未知异常，系统无法处理
        super(DaxPayErrorCode.SYSTEM_UNKNOWN_ERROR,"未知异常，系统无法处理");
        initMessageKey("pay.error.systemUnknown");
    }
}
