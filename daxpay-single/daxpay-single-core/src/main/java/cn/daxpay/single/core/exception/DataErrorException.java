package cn.daxpay.single.core.exception;

import cn.daxpay.single.core.code.DaxPayErrorCode;

/**
 * 数据错误
 * @author xxm
 * @since 2024/6/17
 */
public class DataErrorException extends PayFailureException{

    public DataErrorException(String message) {
        super(DaxPayErrorCode.DATA_ERROR,message);
    }

    public DataErrorException() {
        super(DaxPayErrorCode.DATA_ERROR,"数据错误");
    }
}
