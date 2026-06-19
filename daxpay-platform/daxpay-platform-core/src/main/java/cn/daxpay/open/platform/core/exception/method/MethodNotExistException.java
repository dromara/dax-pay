package cn.daxpay.open.platform.core.exception.method;

import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.PayFailureException;

/// # 不存在的支付方式
///
public class MethodNotExistException extends PayFailureException {

    /// 使用默认错误码与 i18n messageKey（勿传已翻译中文）
    public MethodNotExistException(String messageKey) {
        super(DaxPayErrorCode.METHOD_NOT_EXIST, messageKey);
    }

    /// 使用默认错误码、messageKey 与 MessageFormat 占位参数
    public MethodNotExistException(String messageKey, Object... args) {
        super(DaxPayErrorCode.METHOD_NOT_EXIST, messageKey, args);
    }

    public MethodNotExistException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }

    public MethodNotExistException() {
        // 不存在的支付方式
        super(DaxPayErrorCode.METHOD_NOT_EXIST,"不存在的支付方式");
        initMessageKey("pay.error.methodNotExist");
    }
}
