package org.dromara.daxpay.platform.core.exception.system;

import org.dromara.daxpay.platform.core.code.DaxPayErrorCode;
import org.dromara.daxpay.platform.core.exception.PayFailureException;

/// # 数据错误
///
public class DataErrorException extends PayFailureException {

    /// 使用默认错误码与 i18n messageKey（勿传已翻译中文）
    public DataErrorException(String messageKey) {
        super(DaxPayErrorCode.DATA_ERROR, messageKey);
    }

    /// 使用默认错误码、messageKey 与 MessageFormat 占位参数
    public DataErrorException(String messageKey, Object... args) {
        super(DaxPayErrorCode.DATA_ERROR, messageKey, args);
    }

    public DataErrorException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }

    public DataErrorException() {
        // 数据错误
        super(DaxPayErrorCode.DATA_ERROR,"数据错误");
        initMessageKey("pay.error.dataError");
    }
}
