package cn.bootx.platform.starter.auth.exception;

/**
 * 用户未找到异常
 *
 * @author xxm
 * @since 2021/7/30
 */
public class UserNotFoundException extends LoginFailureException {

    public UserNotFoundException(String account) {
        super(account, "用户未找到");
    }

    public UserNotFoundException() {
        super("用户未找到");
    }

}
