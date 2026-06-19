package cn.daxpay.open.platform.core.exception.config;

import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.PayFailureException;

/// # 配置错误
///
public class ConfigErrorException extends PayFailureException {

    /// 使用默认错误码与 i18n messageKey（勿传已翻译中文）
    public ConfigErrorException(String messageKey) {
        super(DaxPayErrorCode.CONFIG_ERROR, messageKey);
    }

    /// 使用默认错误码、messageKey 与 MessageFormat 占位参数
    public ConfigErrorException(String messageKey, Object... args) {
        super(DaxPayErrorCode.CONFIG_ERROR, messageKey, args);
    }

    public ConfigErrorException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }

    public ConfigErrorException() {
        // 配置错误
        super(DaxPayErrorCode.CONFIG_ERROR,"配置错误");
        initMessageKey("pay.error.configError");
    }
}
