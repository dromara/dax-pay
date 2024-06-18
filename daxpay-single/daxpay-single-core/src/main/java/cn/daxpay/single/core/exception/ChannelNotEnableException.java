package cn.daxpay.single.core.exception;

import cn.daxpay.single.core.code.DaxPayCommonErrorCode;

/**
 * 支付通道未启用
 * @author xxm
 * @since 2024/6/17
 */
public class ChannelNotEnableException extends PayFailureException{

    public ChannelNotEnableException(String message) {
        super(DaxPayCommonErrorCode.CHANNEL_NOT_ENABLE,message);
    }

    public ChannelNotEnableException() {
        super(DaxPayCommonErrorCode.CHANNEL_NOT_ENABLE,"支付通道未启用");
    }
}
