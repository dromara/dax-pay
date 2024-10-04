package cn.bootx.platform.starter.auth.exception;

import cn.bootx.platform.core.exception.BizException;

/**
 * 终端方式被停用
 *
 * @author xxm
 * @since 2021/9/7
 */
public class ClientNotEnableException extends BizException {

    public ClientNotEnableException() {
        super("指定终端方式已被停用");
    }

}
