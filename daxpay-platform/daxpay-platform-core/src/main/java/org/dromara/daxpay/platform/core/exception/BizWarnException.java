package org.dromara.daxpay.platform.core.exception;

/// # 警告业务异常
///
/// warn 级别，由 `RestExceptionHandler` 记录 warn 日志。抛错须使用 i18n messageKey。
///
/// @see BizException 基类与构造器约定
public class BizWarnException extends BizException {

    /// 指定错误码与 messageKey
    public BizWarnException(int code, String messageKey) {
        super(code, messageKey);
    }

    /// 使用默认错误码与 messageKey
    public BizWarnException(String messageKey) {
        super(messageKey);
    }

    /// 指定错误码、messageKey 与占位符参数
    public BizWarnException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }
}
