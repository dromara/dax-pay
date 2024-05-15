package cn.bootx.platform.common.core.exception;

import java.io.Serializable;

/**
 * 系统类异常
 */
public abstract class SystemException extends ErrorCodeRuntimeException implements Serializable {

    private static final long serialVersionUID = -9173431377871930556L;

    public SystemException(int code, String message) {
        super(code, message);
    }

    public SystemException() {
        super();
    }

}
