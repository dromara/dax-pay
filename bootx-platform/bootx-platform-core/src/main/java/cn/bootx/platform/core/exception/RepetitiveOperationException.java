package cn.bootx.platform.core.exception;

import java.io.Serializable;

import static cn.bootx.platform.core.code.CommonErrorCode.REPETITIVE_OPERATION_ERROR;


/**
 * 重复操作异常
 *
 * @author xxm
 * @since 2021/1/2
 */
public class RepetitiveOperationException extends BizInfoException implements Serializable {

    public RepetitiveOperationException() {
        super(REPETITIVE_OPERATION_ERROR, "重复操作异常");
    }

    public RepetitiveOperationException(String msg) {
        super(REPETITIVE_OPERATION_ERROR, msg);
    }

}
