package org.dromara.daxpay.platform.core.exception.business;

import org.dromara.daxpay.platform.core.code.DaxPayErrorCode;
import org.dromara.daxpay.platform.core.exception.PayFailureException;

/// # 验签失败
///
public class VerifySignFailedException extends PayFailureException {

    /// 使用默认错误码与 i18n messageKey（勿传已翻译中文）
    public VerifySignFailedException(String messageKey) {
        super(DaxPayErrorCode.VERIFY_SIGN_FAILED, messageKey);
    }

    /// 使用默认错误码、messageKey 与 MessageFormat 占位参数
    public VerifySignFailedException(String messageKey, Object... args) {
        super(DaxPayErrorCode.VERIFY_SIGN_FAILED, messageKey, args);
    }

    public VerifySignFailedException() {
        // 验签失败
        super(DaxPayErrorCode.VERIFY_SIGN_FAILED,"验签失败");
        initMessageKey("pay.error.signVerifyFail");
    }
}
