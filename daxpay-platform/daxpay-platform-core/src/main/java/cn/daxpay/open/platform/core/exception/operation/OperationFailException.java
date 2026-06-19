package cn.daxpay.open.platform.core.exception.operation;

import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.PayFailureException;

/// # 操作失败
///
public class OperationFailException extends PayFailureException {

    /// 使用默认错误码与 i18n messageKey（勿传已翻译中文）
    public OperationFailException(String messageKey) {
        super(DaxPayErrorCode.OPERATION_FAIL, messageKey);
    }

    /// 使用默认错误码、messageKey 与 MessageFormat 占位参数
    public OperationFailException(String messageKey, Object... args) {
        super(DaxPayErrorCode.OPERATION_FAIL, messageKey, args);
    }

    public OperationFailException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }

    public OperationFailException() {
        super(DaxPayErrorCode.OPERATION_FAIL,"操作失败");
        initMessageKey("pay.error.operateFailed");
    }
}
