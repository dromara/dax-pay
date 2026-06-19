package cn.daxpay.open.platform.core.exception.channel;

import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.PayFailureException;

/// # 不存在的支付通道
///
public class ChannelNotExistException extends PayFailureException {

    public ChannelNotExistException(String messageKey) {
        super(DaxPayErrorCode.CHANNEL_NOT_EXIST, messageKey);
    }

    public ChannelNotExistException() {
        super(DaxPayErrorCode.CHANNEL_NOT_EXIST, "pay.error.channelNotExist");
        initMessageKey("pay.error.channelNotExist");
    }
}
