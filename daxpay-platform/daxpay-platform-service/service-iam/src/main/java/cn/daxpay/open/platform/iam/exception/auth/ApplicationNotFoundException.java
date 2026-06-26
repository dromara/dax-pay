package cn.daxpay.open.platform.iam.exception.auth;

import cn.daxpay.open.platform.core.exception.BizInfoException;

/// # 应用不存在
///
public class ApplicationNotFoundException extends BizInfoException {

    public ApplicationNotFoundException() {
        super("error.auth.applicationNotFound");
    }


    public ApplicationNotFoundException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }
}
