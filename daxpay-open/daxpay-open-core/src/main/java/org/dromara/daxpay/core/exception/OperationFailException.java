package org.dromara.daxpay.core.exception;

import org.dromara.daxpay.core.code.DaxPayErrorCode;

/**
 * 操作失败
 * @author xxm
 * @since 2024/6/17
 */
public class OperationFailException extends PayFailureException{

    public OperationFailException(String message) {
        super(DaxPayErrorCode.OPERATION_FAIL,message);
    }

    public OperationFailException() {
        super(DaxPayErrorCode.OPERATION_FAIL,"操作失败");
    }
}
