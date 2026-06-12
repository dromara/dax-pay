package org.dromara.daxpay.platform.core.exception.method;

import org.dromara.daxpay.platform.core.code.DaxPayErrorCode;
import org.dromara.daxpay.platform.core.exception.PayFailureException;

/// # 支付方式未启用
///
public class MethodNotEnableException extends PayFailureException {

    /// 使用默认错误码与 i18n messageKey（勿传已翻译中文）
    public MethodNotEnableException(String messageKey) {
        super(DaxPayErrorCode.METHOD_NOT_ENABLE, messageKey);
    }

    /// 使用默认错误码、messageKey 与 MessageFormat 占位参数
    public MethodNotEnableException(String messageKey, Object... args) {
        super(DaxPayErrorCode.METHOD_NOT_ENABLE, messageKey, args);
    }

    public MethodNotEnableException() {
        // 支付方式未启用
        super(DaxPayErrorCode.METHOD_NOT_ENABLE,"支付方式未启用");
        initMessageKey("pay.error.methodNotEnable");
    }
}
