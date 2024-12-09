package cn.bootx.platform.starter.auth.exception;

import cn.bootx.platform.core.exception.BizInfoException;

/**
 * 路径检查异常
 *
 * @author xxm
 * @since 2021/12/21
 */
public class RouterCheckException extends BizInfoException {

    public RouterCheckException() {
        super("未登录或不拥有改请求路径的请求权限");
    }

}
