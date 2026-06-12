package org.dromara.daxpay.platform.core.exception.operation;

import org.dromara.daxpay.platform.core.code.DaxPayErrorCode;
import org.dromara.daxpay.platform.core.exception.PayFailureException;

/// # 操作处理中, 请勿重复操作
///
public class OperationProcessingException extends PayFailureException {

    /// 使用默认错误码与 i18n messageKey（勿传已翻译中文）
    public OperationProcessingException(String messageKey) {
        super(DaxPayErrorCode.OPERATION_PROCESSING, messageKey);
    }

    /// 使用默认错误码、messageKey 与 MessageFormat 占位参数
    public OperationProcessingException(String messageKey, Object... args) {
        super(DaxPayErrorCode.OPERATION_PROCESSING, messageKey, args);
    }

    public OperationProcessingException() {
        // 操作处理中, 请勿重复操作
        super(DaxPayErrorCode.OPERATION_PROCESSING,"操作处理中, 请勿重复操作");
        initMessageKey("pay.error.operateProcessing");
    }
}
