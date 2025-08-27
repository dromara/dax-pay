package org.dromara.daxpay.core.exception;

import org.dromara.daxpay.core.code.DaxPayErrorCode;

/**
 * 不支持的操作
 * @author xxm
 * @since 2024/6/17
 */
public class OperationUnsupportedException extends PayFailureException{

    public OperationUnsupportedException(String message) {
        super(DaxPayErrorCode.OPERATION_UNSUPPORTED,message);
    }

    public OperationUnsupportedException() {
        super(DaxPayErrorCode.OPERATION_UNSUPPORTED,"不支持的操作");
    }
}
