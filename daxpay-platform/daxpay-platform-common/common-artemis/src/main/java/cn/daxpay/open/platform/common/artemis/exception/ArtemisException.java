package cn.daxpay.open.platform.common.artemis.exception;

import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.BizErrorException;

/// # Artemis 异常
///
/// JMS 消息发送与消息转换失败时使用，messageKey 前缀为 `error.artemis.*`。
///
/// @see BizErrorException 基类与构造器约定
public class ArtemisException extends BizErrorException {

    /// 使用默认错误码与 messageKey
    public ArtemisException(String messageKey) {
        super(messageKey);
    }

    /// 指定错误码与 messageKey
    public ArtemisException(int code, String messageKey) {
        super(code, messageKey);
    }

    /// 使用默认错误码、messageKey 与占位符参数
    public ArtemisException(String messageKey, Object... args) {
        super(CommonCode.FAIL_CODE, messageKey, args);
    }
}
