package org.dromara.daxpay.core.exception;

import org.dromara.daxpay.core.code.DaxPayErrorCode;

/**
 * 不存在的状态
 * @author xxm
 * @since 2024/6/17
 */
public class StatusNotExistException extends PayFailureException{

    public StatusNotExistException(String message) {
        super(DaxPayErrorCode.STATUS_NOT_EXIST,message);
    }

    public StatusNotExistException() {
        super(DaxPayErrorCode.STATUS_NOT_EXIST,"不存在的状态");
    }
}
