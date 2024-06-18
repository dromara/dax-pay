package cn.daxpay.single.core.exception;

import cn.daxpay.single.core.code.DaxPayCommonErrorCode;

/**
 * 操作失败
 * @author xxm
 * @since 2024/6/17
 */
public class OperationFailedException extends PayFailureException{

    public OperationFailedException(String message) {
        super(DaxPayCommonErrorCode.OPERATION_FAILED,message);
    }

    public OperationFailedException() {
        super(DaxPayCommonErrorCode.OPERATION_FAILED,"操作失败");
    }
}
