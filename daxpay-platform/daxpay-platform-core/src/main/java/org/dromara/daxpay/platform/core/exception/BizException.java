package org.dromara.daxpay.platform.core.exception;

import org.dromara.daxpay.platform.core.code.CommonCode;
import lombok.Getter;

/// # 业务异常基类
///
/// 对外 API 抛错须使用 i18n messageKey，由 `RestExceptionHandler` 经 `I18nUtil` 解析为本地化文案。
///
/// ## 构造器约定
/// - `(int code, String messageKey)`：指定错误码与消息 key；与 `(int, String, Object...)` 并存时，两实参调用优先匹配本方法
/// - `(int code, String messageKey, Object... args)`：带 MessageFormat 占位符 `{0}`、`{1}` 的参数
/// - `(String messageKey)` / `(String messageKey, Object... args)`：默认使用 `CommonCode.FAIL_CODE`
/// - 无参构造：仅子类在无参构造中通过 `initMessageKey` 补充 key
///
/// @see BizErrorException 致命异常，error 级别
/// @see BizWarnException 警告异常，warn 级别
/// @see BizInfoException 普通业务异常，info 级别，不打印堆栈
@Getter
public class BizException extends RuntimeException {

    /// HTTP/业务响应错误码
    private int code = CommonCode.FAIL_CODE;

    /// 国际化消息 key（对应 common-i18n 资源）
    private String messageKey;

    /// 国际化消息占位符参数
    private Object[] args;

    /// 设置国际化消息 key（供子类无参构造器在 super 之后调用）
    protected void initMessageKey(String messageKey, Object... args) {
        this.messageKey = messageKey;
        this.args = args;
    }

    /// 指定错误码与 messageKey（两参数形式，Java 优先于 varargs 构造）
    public BizException(int code, String messageKey) {
        super(messageKey);
        this.code = code;
        this.messageKey = messageKey;
    }

    /// 指定错误码、messageKey 与占位符参数
    public BizException(int code, String messageKey, Object... args) {
        super(messageKey);
        this.code = code;
        this.messageKey = messageKey;
        this.args = args;
    }

    /// 使用默认错误码与 messageKey
    public BizException(String messageKey) {
        this(CommonCode.FAIL_CODE, messageKey);
    }

    /// 使用默认错误码、messageKey 与占位符参数
    public BizException(String messageKey, Object... args) {
        this(CommonCode.FAIL_CODE, messageKey, args);
    }

    /// 无参构造，由子类设置 code / messageKey
    public BizException() {
    }
}
