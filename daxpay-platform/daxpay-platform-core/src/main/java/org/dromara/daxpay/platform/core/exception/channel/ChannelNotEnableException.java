package org.dromara.daxpay.platform.core.exception.channel;

import org.dromara.daxpay.platform.core.code.DaxPayErrorCode;
import org.dromara.daxpay.platform.core.exception.PayFailureException;

/// # 支付通道未启用
///
public class ChannelNotEnableException extends PayFailureException {

    /// 使用默认错误码与 i18n messageKey（勿传已翻译中文）
    public ChannelNotEnableException(String messageKey) {
        super(DaxPayErrorCode.CHANNEL_NOT_ENABLE, messageKey);
    }

    /// 使用默认错误码、messageKey 与 MessageFormat 占位参数
    public ChannelNotEnableException(String messageKey, Object... args) {
        super(DaxPayErrorCode.CHANNEL_NOT_ENABLE, messageKey, args);
    }

    public ChannelNotEnableException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }

    public ChannelNotEnableException() {
        // 支付通道未启用
        super(DaxPayErrorCode.CHANNEL_NOT_ENABLE,"支付通道未启用");
        initMessageKey("pay.error.channelNotEnable");
    }
}
