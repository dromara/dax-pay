package org.dromara.daxpay.platform.capability.auth.exception;

import org.dromara.daxpay.platform.core.exception.BizInfoException;

/// # 应用不存在
///
public class ApplicationNotFoundException extends BizInfoException {

    public ApplicationNotFoundException() {
        super("未找到对应的应用");
    }


    public ApplicationNotFoundException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }
}
