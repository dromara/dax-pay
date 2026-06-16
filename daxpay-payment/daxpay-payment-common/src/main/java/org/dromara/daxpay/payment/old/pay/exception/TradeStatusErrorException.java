package org.dromara.daxpay.payment.old.pay.exception;

import org.dromara.daxpay.platform.core.code.DaxPayErrorCode;
import org.dromara.daxpay.platform.core.exception.PayFailureException;

/// # 交易状态错误
///
public class TradeStatusErrorException extends PayFailureException {

    public TradeStatusErrorException(String message) {
        super(DaxPayErrorCode.TRADE_STATUS_ERROR,message);
    }

    public TradeStatusErrorException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }

    public TradeStatusErrorException() {
        // 交易状态错误
        super(DaxPayErrorCode.TRADE_STATUS_ERROR,"交易状态错误");
        initMessageKey("pay.error.statusError");
    }
}
