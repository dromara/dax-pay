package cn.daxpay.single.core.exception;

import cn.daxpay.single.core.code.DaxPayCommonErrorCode;

/**
 * 不存在的状态
 * @author xxm
 * @since 2024/6/17
 */
public class StatusNotExistException extends PayFailureException{

    public StatusNotExistException(String message) {
        super(DaxPayCommonErrorCode.STATUS_NOT_EXIST,message);
    }

    public StatusNotExistException() {
        super(DaxPayCommonErrorCode.STATUS_NOT_EXIST,"不存在的状态");
    }
}
