package cn.daxpay.multi.core.exception;

import cn.daxpay.multi.core.code.DaxPayCommonErrorCode;

/**
 * 支付通道未启用
 * @author xxm
 * @since 2024/6/17
 */
public class ChannelNotEnabledErrorException extends PayFailureException{

    public ChannelNotEnabledErrorException(String message) {
        super(DaxPayCommonErrorCode.CHANNEL_NOT_ENABLED,message);
    }

    public ChannelNotEnabledErrorException() {
        super(DaxPayCommonErrorCode.CHANNEL_NOT_ENABLED,"支付通道未启用");
    }
}
