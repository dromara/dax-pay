package cn.daxpay.open.platform.core.exception;

import cn.daxpay.open.platform.core.code.CommonErrorCode;

/// # 验证失败异常
///
public class ValidationFailedException extends BizInfoException {

    /// 使用 i18n messageKey（勿传已翻译中文）；无占位符时仅传 key
    public ValidationFailedException(String messageKey) {
        super(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, messageKey);
    }

    /// 使用 messageKey 与 MessageFormat 占位参数
    public ValidationFailedException(String messageKey, Object... args) {
        super(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, messageKey, args);
    }

    public ValidationFailedException() {
        super(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "验证参数错误");
        initMessageKey("error.common.validateParams");
    }

}
