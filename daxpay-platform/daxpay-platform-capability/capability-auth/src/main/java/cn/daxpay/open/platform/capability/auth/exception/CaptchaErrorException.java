package cn.daxpay.open.platform.capability.auth.exception;

import cn.daxpay.open.platform.core.exception.BizInfoException;

/// # 验证码错误异常
///
public class CaptchaErrorException extends BizInfoException {

    public CaptchaErrorException() {
        super(40002, "验证码错误");
        initMessageKey("error.auth.auth.captchaError");
    }

    public CaptchaErrorException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }
}
