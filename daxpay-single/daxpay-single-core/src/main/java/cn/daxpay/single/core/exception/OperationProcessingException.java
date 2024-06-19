package cn.daxpay.single.core.exception;

import cn.daxpay.single.core.code.DaxPayErrorCode;

/**
 * 操作处理中, 请勿重复操作
 * @author xxm
 * @since 2024/6/17
 */
public class OperationProcessingException extends PayFailureException{

    public OperationProcessingException(String message) {
        super(DaxPayErrorCode.OPERATION_PROCESSING,message);
    }

    public OperationProcessingException() {
        super(DaxPayErrorCode.OPERATION_PROCESSING,"操作处理中, 请勿重复操作");
    }
}
