package org.dromara.daxpay.payment.pay.exception;

import org.dromara.daxpay.payment.common.code.DaxPayErrorCode;
import org.dromara.daxpay.payment.common.exception.PayFailureException;

/**
 * 交易失败
 * @author xxm
 * @since 2024/6/17
 */
public class TradeFailException extends PayFailureException {

    public TradeFailException(String message) {
        super(DaxPayErrorCode.TRADE_FAIL,message);
    }

    public TradeFailException() {
        super(DaxPayErrorCode.TRADE_FAIL,"交易失败");
    }
}
