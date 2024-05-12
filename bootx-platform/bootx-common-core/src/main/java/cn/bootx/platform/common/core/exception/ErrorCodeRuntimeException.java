package cn.bootx.platform.common.core.exception;

import lombok.Getter;

import java.io.Serializable;

/**
 * 错误码异常基类
 */
@Getter
public abstract class ErrorCodeRuntimeException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1724988277326336635L;

    private int code;

    public ErrorCodeRuntimeException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ErrorCodeRuntimeException() {
        super();
    }

    public void setCode(int code) {
        this.code = code;
    }

}
