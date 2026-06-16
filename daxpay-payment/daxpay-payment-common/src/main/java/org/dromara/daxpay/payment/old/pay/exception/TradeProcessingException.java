package org.dromara.daxpay.payment.old.pay.exception;

import org.dromara.daxpay.platform.core.code.DaxPayErrorCode;
import org.dromara.daxpay.platform.core.exception.PayFailureException;

/// # 交易处理中, 请勿重复操作
///
public class TradeProcessingException extends PayFailureException {

    public TradeProcessingException(String message) {
        super(DaxPayErrorCode.TRADE_PROCESSING,message);
    }

    public TradeProcessingException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }

    public TradeProcessingException() {
        // 交易处理中，请勿重复操作
        super(DaxPayErrorCode.TRADE_PROCESSING,"交易处理中，请勿重复操作");
        initMessageKey("pay.error.processing");
    }
}
