package cn.daxpay.open.payment.old.pay.exception;

import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.PayFailureException;

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
