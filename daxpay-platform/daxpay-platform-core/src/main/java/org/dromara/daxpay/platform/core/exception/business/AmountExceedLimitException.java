package org.dromara.daxpay.platform.core.exception.business;

import org.dromara.daxpay.platform.core.code.DaxPayErrorCode;
import org.dromara.daxpay.platform.core.exception.PayFailureException;

/// # 金额超过限额
///
public class AmountExceedLimitException extends PayFailureException {

    /// 使用默认错误码与 i18n messageKey（勿传已翻译中文）
    public AmountExceedLimitException(String messageKey) {
        super(DaxPayErrorCode.AMOUNT_EXCEED_LIMIT, messageKey);
    }

    /// 使用默认错误码、messageKey 与 MessageFormat 占位参数
    public AmountExceedLimitException(String messageKey, Object... args) {
        super(DaxPayErrorCode.AMOUNT_EXCEED_LIMIT, messageKey, args);
    }

    public AmountExceedLimitException() {
        // 金额超过限额
        super(DaxPayErrorCode.AMOUNT_EXCEED_LIMIT,"金额超过限额");
        initMessageKey("pay.error.amount.exceed");
    }
}
