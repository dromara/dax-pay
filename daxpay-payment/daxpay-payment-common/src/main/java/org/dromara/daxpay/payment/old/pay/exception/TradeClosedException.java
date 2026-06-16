package org.dromara.daxpay.payment.old.pay.exception;

import org.dromara.daxpay.platform.core.code.DaxPayErrorCode;
import org.dromara.daxpay.platform.core.exception.PayFailureException;

/// # 交易已关闭
///
public class TradeClosedException extends PayFailureException {

    public TradeClosedException(String message) {
        super(DaxPayErrorCode.TRADE_CLOSED,message);
    }

    public TradeClosedException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }

    public TradeClosedException() {
        // 交易已关闭
        super(DaxPayErrorCode.TRADE_CLOSED,"交易已关闭");
        initMessageKey("pay.error.closed");
    }
}
