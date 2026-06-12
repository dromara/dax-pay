package org.dromara.daxpay.platform.capability.auth.exception;

import org.dromara.daxpay.platform.core.exception.BizInfoException;

/// # 需要验证码异常
///
public class CaptchaRequiredException extends BizInfoException {

    private final String captchaKey;

    public CaptchaRequiredException(String captchaKey) {
        super(40001, "请输入验证码");
        this.captchaKey = captchaKey;
    }

    public String getCaptchaKey() {
        return captchaKey;
    }

    public CaptchaRequiredException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
        this.captchaKey = null;
    }
}
