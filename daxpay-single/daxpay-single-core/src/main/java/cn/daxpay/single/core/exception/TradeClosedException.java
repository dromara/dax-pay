package cn.daxpay.single.core.exception;

import cn.daxpay.single.core.code.DaxPayCommonErrorCode;

/**
 * 交易已关闭
 * @author xxm
 * @since 2024/6/17
 */
public class TradeClosedException extends PayFailureException{

    public TradeClosedException(String message) {
        super(DaxPayCommonErrorCode.TRADE_CLOSED,message);
    }

    public TradeClosedException() {
        super(DaxPayCommonErrorCode.TRADE_CLOSED,"交易已关闭");
    }
}
