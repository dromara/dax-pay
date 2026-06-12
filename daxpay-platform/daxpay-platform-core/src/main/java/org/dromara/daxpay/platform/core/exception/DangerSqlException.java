package org.dromara.daxpay.platform.core.exception;

import org.dromara.daxpay.platform.core.code.CommonErrorCode;

/// # SQL相关异常
///
public class DangerSqlException extends BizException {

    /// 使用 i18n messageKey（勿传已翻译中文）
    public DangerSqlException(String messageKey) {
        super(CommonErrorCode.DANGER_SQL, messageKey);
    }

    /// 使用 messageKey 与 MessageFormat 占位参数
    public DangerSqlException(String messageKey, Object... args) {
        super(CommonErrorCode.DANGER_SQL, messageKey, args);
    }

    public DangerSqlException() {
        super(CommonErrorCode.DANGER_SQL, "危险SQL异常");
        initMessageKey("error.common.dangerSql");
    }


    public DangerSqlException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }
}
