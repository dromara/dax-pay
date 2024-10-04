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
        super("没有对应请求路径的权限");
    }

}
