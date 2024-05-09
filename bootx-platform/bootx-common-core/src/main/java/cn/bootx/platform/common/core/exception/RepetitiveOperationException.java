package cn.bootx.platform.common.core.exception;

import java.io.Serializable;

import static cn.bootx.platform.common.core.code.CommonErrorCode.REPETITIVE_OPERATION_ERROR;

/**
 * 重复操作异常
 *
 * @author xxm
 * @since 2021/1/2
 */
public class RepetitiveOperationException extends SystemException implements Serializable {

    private static final long serialVersionUID = 2120383728758502943L;

    public RepetitiveOperationException() {
        super(REPETITIVE_OPERATION_ERROR, "重复操作异常");
    }

    public RepetitiveOperationException(String msg) {
        super(REPETITIVE_OPERATION_ERROR, msg);
    }

}
