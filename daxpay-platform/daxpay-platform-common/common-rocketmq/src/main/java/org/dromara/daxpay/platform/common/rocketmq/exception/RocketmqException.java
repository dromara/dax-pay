package org.dromara.daxpay.platform.common.rocketmq.exception;

import org.dromara.daxpay.platform.core.code.CommonCode;
import org.dromara.daxpay.platform.core.exception.BizErrorException;

/// # RocketMQ 异常
///
/// MQ 发送与消息转换失败时使用，messageKey 前缀为 `error.rocketmq.*`。
///
/// @see BizException 基类与构造器约定
public class RocketmqException extends BizErrorException {

    /// 使用默认错误码与 messageKey
    public RocketmqException(String messageKey) {
        super(messageKey);
    }

    /// 指定错误码与 messageKey
    public RocketmqException(int code, String messageKey) {
        super(code, messageKey);
    }

    /// 使用默认错误码、messageKey 与占位符参数
    public RocketmqException(String messageKey, Object... args) {
        super(CommonCode.FAIL_CODE, messageKey, args);
    }
}
