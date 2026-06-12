package org.dromara.daxpay.platform.core.exception.operation;

import org.dromara.daxpay.platform.core.code.DaxPayErrorCode;
import org.dromara.daxpay.platform.core.exception.PayFailureException;

/// # 不支持的操作
///
public class OperationUnsupportedException extends PayFailureException {

    /// 使用默认错误码与 i18n messageKey（勿传已翻译中文）
    public OperationUnsupportedException(String messageKey) {
        super(DaxPayErrorCode.OPERATION_UNSUPPORTED, messageKey);
    }

    /// 使用默认错误码、messageKey 与 MessageFormat 占位参数
    public OperationUnsupportedException(String messageKey, Object... args) {
        super(DaxPayErrorCode.OPERATION_UNSUPPORTED, messageKey, args);
    }

    public OperationUnsupportedException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }

    public OperationUnsupportedException() {
        // 不支持的操作
        super(DaxPayErrorCode.OPERATION_UNSUPPORTED,"不支持的操作");
        initMessageKey("pay.error.unsupportedOperate");
    }
}
