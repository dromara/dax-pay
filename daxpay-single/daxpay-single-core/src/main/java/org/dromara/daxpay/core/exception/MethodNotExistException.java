package org.dromara.daxpay.core.exception;

import org.dromara.daxpay.core.code.DaxPayErrorCode;

/**
 * 不存在的支付方式
 * @author xxm
 * @since 2024/6/17
 */
public class MethodNotExistException extends PayFailureException{

    public MethodNotExistException(String message) {
        super(DaxPayErrorCode.METHOD_NOT_EXIST,message);
    }

    public MethodNotExistException() {
        super(DaxPayErrorCode.METHOD_NOT_EXIST,"不存在的支付方式");
    }
}
