package cn.bootx.platform.starter.auth.exception;

import cn.bootx.platform.core.exception.BizException;
import lombok.Getter;

/**
 * 登录错误异常
 *
 * @author xxm
 * @since 2021/8/13
 */
@Getter
public class LoginFailureException extends BizException {

    private final String account;

    public LoginFailureException(String message) {
        super(message);
        this.account = "未知";
    }

    public LoginFailureException(String account, String message) {
        super(message);
        this.account = account;
    }

}
