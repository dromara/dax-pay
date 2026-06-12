package org.dromara.daxpay.payment.pay.exception;

import org.dromara.daxpay.platform.core.code.DaxPayErrorCode;
import org.dromara.daxpay.platform.core.exception.PayFailureException;

/// # 交易失败
///
public class TradeFailException extends PayFailureException {

    /// 使用默认错误码与 i18n messageKey（勿传已翻译中文）
    public TradeFailException(String messageKey) {
        super(DaxPayErrorCode.TRADE_FAIL, messageKey);
    }

    /// 使用默认错误码、messageKey 与 MessageFormat 占位参数
    public TradeFailException(String messageKey, Object... args) {
        super(DaxPayErrorCode.TRADE_FAIL, messageKey, args);
    }

    public TradeFailException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }

    public TradeFailException() {
        // 交易失败
        super(DaxPayErrorCode.TRADE_FAIL,"交易失败");
        initMessageKey("pay.error.failed");
    }
}
