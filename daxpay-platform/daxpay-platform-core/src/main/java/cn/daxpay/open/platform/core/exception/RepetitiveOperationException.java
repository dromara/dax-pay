package cn.daxpay.open.platform.core.exception;

import cn.daxpay.open.platform.core.code.CommonErrorCode;

/// # 重复操作异常
///
public class RepetitiveOperationException extends BizInfoException {

    public RepetitiveOperationException() {
        // 重复操作异常
        super(CommonErrorCode.REPETITIVE_OPERATION_ERROR, "重复操作异常");
        initMessageKey("error.common.repetitiveOperation");
    }

    public RepetitiveOperationException(String msg) {
        super(CommonErrorCode.REPETITIVE_OPERATION_ERROR, msg);
    }

}
