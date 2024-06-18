package cn.daxpay.single.core.exception;

import cn.daxpay.single.core.code.DaxPayCommonErrorCode;

/**
 * 支付通道未启用
 * @author xxm
 * @since 2024/6/17
 */
public class ChannelNotEnabledException extends PayFailureException{

    public ChannelNotEnabledException(String message) {
        super(DaxPayCommonErrorCode.CHANNEL_NOT_ENABLED,message);
    }

    public ChannelNotEnabledException() {
        super(DaxPayCommonErrorCode.CHANNEL_NOT_ENABLED,"支付通道未启用");
    }
}
