package org.dromara.daxpay.payment.pay.exception;

import org.dromara.daxpay.payment.common.code.DaxPayErrorCode;
import org.dromara.daxpay.payment.common.exception.PayFailureException;

/**
 * 交易已关闭
 * @author xxm
 * @since 2024/6/17
 */
public class TradeClosedException extends PayFailureException {

    public TradeClosedException(String message) {
        super(DaxPayErrorCode.TRADE_CLOSED,message);
    }

    public TradeClosedException() {
        super(DaxPayErrorCode.TRADE_CLOSED,"交易已关闭");
    }
}
