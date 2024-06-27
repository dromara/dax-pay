package cn.daxpay.single.core.exception;

import cn.daxpay.single.core.code.DaxPayErrorCode;

/**
 * 交易已关闭
 * @author xxm
 * @since 2024/6/17
 */
public class TradeClosedException extends PayFailureException{

    public TradeClosedException(String message) {
        super(DaxPayErrorCode.TRADE_CLOSED,message);
    }

    public TradeClosedException() {
        super(DaxPayErrorCode.TRADE_CLOSED,"交易已关闭");
    }
}
