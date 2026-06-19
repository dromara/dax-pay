package cn.daxpay.open.platform.core.exception;

import static cn.daxpay.open.platform.core.code.CommonErrorCode.REPETITIVE_OPERATION_ERROR;

/// # 重复操作异常
///
public class RepetitiveOperationException extends BizInfoException {

    public RepetitiveOperationException() {
        // 重复操作异常
        super(REPETITIVE_OPERATION_ERROR, "重复操作异常");
        initMessageKey("error.common.repetitiveOperation");
    }

    public RepetitiveOperationException(String msg) {
        super(REPETITIVE_OPERATION_ERROR, msg);
    }

}
