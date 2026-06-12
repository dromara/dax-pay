package org.dromara.daxpay.platform.capability.auth.exception;

import org.dromara.daxpay.platform.core.exception.BizException;

/// # 应用被停用
///
public class ApplicationNotEnableException extends BizException {

    public ApplicationNotEnableException() {
        // 指定应用已被停用
        super("指定应用已被停用");
        initMessageKey("error.auth.auth.applicationNotEnable");
    }


    public ApplicationNotEnableException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }
}
