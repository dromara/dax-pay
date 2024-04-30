package cn.bootx.platform.core.exception;

import cn.bootx.platform.core.code.CommonCode;


/**
 * 业务异常基类
 */
public class BizException extends RuntimeException {

    private int code = CommonCode.FAIL_CODE;

    public BizException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BizException(String message) {
        super(message);
    }

    public BizException() {
    }
}
