package cn.daxpay.multi.core.exception.common;

import cn.daxpay.multi.core.code.DaxPayCommonErrorCode;

/**
 *
 * @author xxm
 * @since 2024/6/17
 */
public class PaymentMethodNotExistException extends PayFailureException{

    public PaymentMethodNotExistException(String message) {
        super(DaxPayCommonErrorCode.PAYMENT_METHOD_NOT_EXIST,message);
    }

    public PaymentMethodNotExistException() {
        super(DaxPayCommonErrorCode.PAYMENT_METHOD_NOT_EXIST,"不存在的支付方式");
    }
}
