package cn.daxpay.single.core.exception;

import cn.daxpay.single.core.code.DaxPayCommonErrorCode;

/**
 * 不存在的支付通道
 * @author xxm
 * @since 2024/6/17
 */
public class ChannelNotExistException extends PayFailureException{

    public ChannelNotExistException(String message) {
        super(DaxPayCommonErrorCode.CHANNEL_NOT_EXIST,message);
    }

    public ChannelNotExistException() {
        super(DaxPayCommonErrorCode.CHANNEL_NOT_EXIST,"不存在的支付通道");
    }
}
