package cn.daxpay.single.core.exception;

import cn.daxpay.single.core.code.DaxPayCommonErrorCode;

/**
 * 不存在的支付方式
 * @author xxm
 * @since 2024/6/17
 */
public class MethodNotExistException extends PayFailureException{

    public MethodNotExistException(String message) {
        super(DaxPayCommonErrorCode.METHOD_NOT_EXIST,message);
    }

    public MethodNotExistException() {
        super(DaxPayCommonErrorCode.METHOD_NOT_EXIST,"不存在的支付方式");
    }
}
