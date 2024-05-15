package cn.bootx.platform.common.core.exception;

import java.io.Serializable;

import static cn.bootx.platform.common.core.code.CommonErrorCode.SYSTEM_ERROR;

/**
 * 致命异常基类
 * <p>
 * 出现此异常意味着程序遇到了致命错误，无法继续执行下去，业务流程被中断。
 * <p>
 */
public class FatalException extends ErrorCodeRuntimeException implements Serializable {

    private static final long serialVersionUID = 6855129787981553994L;

    public FatalException(int code, String message) {
        super(code, message);
    }

    public FatalException(String msg) {
        super(SYSTEM_ERROR, msg);
    }

    public FatalException() {
        super();
    }

}
