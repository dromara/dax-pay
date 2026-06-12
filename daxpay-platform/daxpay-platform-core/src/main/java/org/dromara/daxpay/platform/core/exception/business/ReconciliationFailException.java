package org.dromara.daxpay.platform.core.exception.business;

import org.dromara.daxpay.platform.core.code.DaxPayErrorCode;
import org.dromara.daxpay.platform.core.exception.PayFailureException;

/// # 对账失败
///
public class ReconciliationFailException extends PayFailureException {

    /// 使用默认错误码与 i18n messageKey（勿传已翻译中文）
    public ReconciliationFailException(String messageKey) {
        super(DaxPayErrorCode.RECONCILE_FAIL, messageKey);
    }

    /// 使用默认错误码、messageKey 与 MessageFormat 占位参数
    public ReconciliationFailException(String messageKey, Object... args) {
        super(DaxPayErrorCode.RECONCILE_FAIL, messageKey, args);
    }

    public ReconciliationFailException() {
        // 对账失败
        super(DaxPayErrorCode.RECONCILE_FAIL,"对账失败");
        initMessageKey("pay.error.reconciliationFail");
    }
}
