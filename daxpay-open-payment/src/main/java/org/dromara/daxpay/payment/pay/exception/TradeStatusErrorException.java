package org.dromara.daxpay.payment.pay.exception;

import org.dromara.daxpay.payment.common.code.DaxPayErrorCode;
import org.dromara.daxpay.payment.common.exception.PayFailureException;

/**
 * 交易状态错误
 * @author xxm
 * @since 2024/6/17
 */
public class TradeStatusErrorException extends PayFailureException {

    public TradeStatusErrorException(String message) {
        super(DaxPayErrorCode.TRADE_STATUS_ERROR,message);
    }

    public TradeStatusErrorException() {
        super(DaxPayErrorCode.TRADE_STATUS_ERROR,"交易状态错误");
    }
}
