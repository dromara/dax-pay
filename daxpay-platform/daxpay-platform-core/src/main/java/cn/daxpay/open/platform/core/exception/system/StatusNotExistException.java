package cn.daxpay.open.platform.core.exception.system;

import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.PayFailureException;

/// # 不存在的状态
///
public class StatusNotExistException extends PayFailureException {

    /// 使用默认错误码与 i18n messageKey（勿传已翻译中文）
    public StatusNotExistException(String messageKey) {
        super(DaxPayErrorCode.STATUS_NOT_EXIST, messageKey);
    }

    /// 使用默认错误码、messageKey 与 MessageFormat 占位参数
    public StatusNotExistException(String messageKey, Object... args) {
        super(DaxPayErrorCode.STATUS_NOT_EXIST, messageKey, args);
    }

    public StatusNotExistException() {
        // 不存在的状态
        super(DaxPayErrorCode.STATUS_NOT_EXIST,"不存在的状态");
        initMessageKey("pay.error.statusError");
    }
}
