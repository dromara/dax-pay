package cn.daxpay.single.core.exception;

import cn.daxpay.single.core.code.DaxPayCommonErrorCode;

/**
 * 金额超过限额
 * @author xxm
 * @since 2024/6/17
 */
public class AmountExceedLimitException extends PayFailureException{

    public AmountExceedLimitException(String message) {
        super(DaxPayCommonErrorCode.AMOUNT_EXCEED_LIMIT,message);
    }

    public AmountExceedLimitException() {
        super(DaxPayCommonErrorCode.AMOUNT_EXCEED_LIMIT,"金额超过限额");
    }
}
