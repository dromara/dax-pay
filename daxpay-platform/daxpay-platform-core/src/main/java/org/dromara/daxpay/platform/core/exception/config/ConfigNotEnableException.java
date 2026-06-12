package org.dromara.daxpay.platform.core.exception.config;

import org.dromara.daxpay.platform.core.code.DaxPayErrorCode;
import org.dromara.daxpay.platform.core.exception.PayFailureException;

/// # 配置未启用
///
public class ConfigNotEnableException extends PayFailureException {

    /// 使用默认错误码与 i18n messageKey（勿传已翻译中文）
    public ConfigNotEnableException(String messageKey) {
        super(DaxPayErrorCode.CONFIG_NOT_ENABLE, messageKey);
    }

    /// 使用默认错误码、messageKey 与 MessageFormat 占位参数
    public ConfigNotEnableException(String messageKey, Object... args) {
        super(DaxPayErrorCode.CONFIG_NOT_ENABLE, messageKey, args);
    }

    public ConfigNotEnableException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }

    public ConfigNotEnableException() {
        // 配置未启用
        super(DaxPayErrorCode.CONFIG_NOT_ENABLE,"配置未启用");
        initMessageKey("pay.error.configNotEnable");
    }
}
