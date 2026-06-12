package org.dromara.daxpay.platform.core.exception;

/// # 普通业务异常
///
/// info 级别，由 `RestExceptionHandler` 处理时不打印堆栈。抛错须使用 i18n messageKey。
///
/// @see BizException 基类与构造器约定
public class BizInfoException extends BizException {

    /// 指定错误码与 messageKey（两实参时优先于 varargs 构造）
    public BizInfoException(int code, String messageKey) {
        super(code, messageKey);
    }

    /// 指定错误码、messageKey 与占位符参数
    public BizInfoException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }

    /// 使用默认错误码与 messageKey
    public BizInfoException(String messageKey) {
        super(messageKey);
    }
}
