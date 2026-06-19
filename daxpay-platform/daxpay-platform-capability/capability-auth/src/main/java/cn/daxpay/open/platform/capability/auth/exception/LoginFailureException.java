package cn.daxpay.open.platform.capability.auth.exception;

import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.Getter;

/// # 登录错误异常
///
@Getter
public class LoginFailureException extends BizInfoException {

    private final String account;

    private final Long userId;

    /// 使用 i18n messageKey（勿传已翻译中文）
    public LoginFailureException(String messageKey) {
        super(messageKey);
        this.account = "未知";
        this.userId = null;
    }

    public LoginFailureException(String account, String message) {
        super(message);
        this.account = account;
        this.userId = null;
    }

    public LoginFailureException(Long userId, String account, String message) {
        super(message);
        this.account = account;
        this.userId = userId;
    }


    public LoginFailureException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
        this.account = null;
        this.userId = null;
    }
}
