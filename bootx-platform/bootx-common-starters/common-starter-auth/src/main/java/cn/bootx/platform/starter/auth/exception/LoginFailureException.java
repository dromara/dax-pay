package cn.bootx.platform.starter.auth.exception;

import cn.bootx.platform.common.core.exception.BizException;
import lombok.Getter;

/**
 * 登录错误异常
 *
 * @author xxm
 * @since 2021/8/13
 */
@Getter
public class LoginFailureException extends BizException {

    private final String username;

    public LoginFailureException(String message) {
        super(message);
        this.username = "未知";
    }

    public LoginFailureException(String username, String message) {
        super(message);
        this.username = username;
    }

}
