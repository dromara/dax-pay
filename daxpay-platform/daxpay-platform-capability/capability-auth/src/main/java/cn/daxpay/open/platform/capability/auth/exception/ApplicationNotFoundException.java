package cn.daxpay.open.platform.capability.auth.exception;

import cn.daxpay.open.platform.core.exception.BizInfoException;

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
