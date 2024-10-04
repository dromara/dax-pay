package org.dromara.daxpay.core.exception;

import org.dromara.daxpay.core.code.DaxPayErrorCode;

/**
 * 不存在的支付通道
 * @author xxm
 * @since 2024/6/17
 */
public class ChannelNotExistException extends PayFailureException{

    public ChannelNotExistException(String message) {
        super(DaxPayErrorCode.CHANNEL_NOT_EXIST,message);
    }

    public ChannelNotExistException() {
        super(DaxPayErrorCode.CHANNEL_NOT_EXIST,"不存在的支付通道");
    }
}
