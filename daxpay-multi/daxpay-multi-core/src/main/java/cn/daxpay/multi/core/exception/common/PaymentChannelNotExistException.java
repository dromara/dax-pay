package cn.daxpay.multi.core.exception.common;

import cn.daxpay.multi.core.code.DaxPayCommonErrorCode;

/**
 *
 * @author xxm
 * @since 2024/6/17
 */
public class PaymentChannelNotExistException extends PayFailureException{

    public PaymentChannelNotExistException(String message) {
        super(DaxPayCommonErrorCode.PAYMENT_CHANNEL_NOT_EXIST,message);
    }

    public PaymentChannelNotExistException() {
        super(DaxPayCommonErrorCode.PAYMENT_CHANNEL_NOT_EXIST,"不存在的支付通道");
    }
}
