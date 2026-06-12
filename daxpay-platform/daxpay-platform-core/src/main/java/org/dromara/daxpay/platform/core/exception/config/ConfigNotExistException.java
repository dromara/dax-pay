package org.dromara.daxpay.platform.core.exception.config;

import org.dromara.daxpay.platform.core.code.DaxPayErrorCode;
import org.dromara.daxpay.platform.core.exception.PayFailureException;

/// # 配置不存在
///
public class ConfigNotExistException extends PayFailureException {

    /// 使用默认错误码与 i18n messageKey（勿传已翻译中文）
    public ConfigNotExistException(String messageKey) {
        super(DaxPayErrorCode.CONFIG_NOT_EXIST, messageKey);
    }

    /// 使用默认错误码、messageKey 与 MessageFormat 占位参数
    public ConfigNotExistException(String messageKey, Object... args) {
        super(DaxPayErrorCode.CONFIG_NOT_EXIST, messageKey, args);
    }

    public ConfigNotExistException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }

    public ConfigNotExistException() {
        // 配置不存在
        super(DaxPayErrorCode.CONFIG_NOT_EXIST,"配置不存在");
        initMessageKey("pay.error.configNotExist");
    }
}
