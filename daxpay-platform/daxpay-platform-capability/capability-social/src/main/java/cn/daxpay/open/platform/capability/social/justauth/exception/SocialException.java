package cn.daxpay.open.platform.capability.social.justauth.exception;

import lombok.Getter;

/// # 社交登录异常
///
@Getter
public class SocialException extends RuntimeException {

    /// 错误码
    private final int code;

    public SocialException(String message) {
        super(message);
        this.code = 5000;
    }

    public SocialException(int code, String message) {
        super(message);
        this.code = code;
    }

    public SocialException(String message, Throwable cause) {
        super(message, cause);
        this.code = 5000;
    }
}
