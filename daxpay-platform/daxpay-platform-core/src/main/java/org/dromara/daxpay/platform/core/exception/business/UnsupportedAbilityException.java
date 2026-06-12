package org.dromara.daxpay.platform.core.exception.business;

import org.dromara.daxpay.platform.core.code.DaxPayErrorCode;
import org.dromara.daxpay.platform.core.exception.PayFailureException;

/// # 不支持该能力
///
public class UnsupportedAbilityException extends PayFailureException {

    /// 使用默认错误码与 i18n messageKey（勿传已翻译中文）
    public UnsupportedAbilityException(String messageKey) {
        super(DaxPayErrorCode.UNSUPPORTED_ABILITY, messageKey);
    }

    /// 使用默认错误码、messageKey 与 MessageFormat 占位参数
    public UnsupportedAbilityException(String messageKey, Object... args) {
        super(DaxPayErrorCode.UNSUPPORTED_ABILITY, messageKey, args);
    }

    public UnsupportedAbilityException() {
        // 不支持该能力
        super(DaxPayErrorCode.UNSUPPORTED_ABILITY,"不支持该能力");
        initMessageKey("pay.error.unsupportedAbility");
    }
}
