package cn.daxpay.single.core.exception;

import cn.daxpay.single.core.code.DaxPayCommonErrorCode;

/**
 * 操作失败
 * @author xxm
 * @since 2024/6/17
 */
public class OperationFailException extends PayFailureException{

    public OperationFailException(String message) {
        super(DaxPayCommonErrorCode.OPERATION_FAIL,message);
    }

    public OperationFailException() {
        super(DaxPayCommonErrorCode.OPERATION_FAIL,"操作失败");
    }
}
