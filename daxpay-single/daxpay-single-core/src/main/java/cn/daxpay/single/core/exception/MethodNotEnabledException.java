package cn.daxpay.single.core.exception;

import cn.daxpay.single.core.code.DaxPayCommonErrorCode;

/**
 * 支付方式未启用
 * @author xxm
 * @since 2024/6/17
 */
public class MethodNotEnabledException extends PayFailureException{

    public MethodNotEnabledException(String message) {
        super(DaxPayCommonErrorCode.METHOD_NOT_ENABLED,message);
    }

    public MethodNotEnabledException() {
        super(DaxPayCommonErrorCode.METHOD_NOT_ENABLED,"支付方式未启用");
    }
}
