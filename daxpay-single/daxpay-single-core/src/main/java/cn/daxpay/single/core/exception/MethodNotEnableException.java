package cn.daxpay.single.core.exception;

import cn.daxpay.single.core.code.DaxPayCommonErrorCode;

/**
 * 支付方式未启用
 * @author xxm
 * @since 2024/6/17
 */
public class MethodNotEnableException extends PayFailureException{

    public MethodNotEnableException(String message) {
        super(DaxPayCommonErrorCode.METHOD_NOT_ENABLE,message);
    }

    public MethodNotEnableException() {
        super(DaxPayCommonErrorCode.METHOD_NOT_ENABLE,"支付方式未启用");
    }
}
