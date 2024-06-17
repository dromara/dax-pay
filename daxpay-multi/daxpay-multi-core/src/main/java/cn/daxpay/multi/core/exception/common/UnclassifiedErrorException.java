package cn.daxpay.multi.core.exception.common;

import cn.daxpay.multi.core.code.DaxPayCommonErrorCode;

/**
 *
 * @author xxm
 * @since 2024/6/17
 */
public class UnclassifiedErrorException extends PayFailureException{

    public UnclassifiedErrorException(String message) {
        super(DaxPayCommonErrorCode.UNCLASSIFIED_ERROR,message);
    }

    public UnclassifiedErrorException() {
        super(DaxPayCommonErrorCode.UNCLASSIFIED_ERROR,"未归类的错误");
    }
}
