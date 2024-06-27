package cn.daxpay.single.core.exception;

import cn.daxpay.single.core.code.DaxPayErrorCode;

/**
 * 未知异常，系统无法处理
 * @author xxm
 * @since 2024/6/17
 */
public class SystemUnknownErrorException extends PayFailureException{

    public SystemUnknownErrorException(String message) {
        super(DaxPayErrorCode.SYSTEM_UNKNOWN_ERROR,message);
    }

    public SystemUnknownErrorException() {
        super(DaxPayErrorCode.SYSTEM_UNKNOWN_ERROR,"未知异常，系统无法处理");
    }
}
