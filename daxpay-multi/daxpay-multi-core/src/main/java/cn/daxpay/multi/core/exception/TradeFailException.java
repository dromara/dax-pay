package cn.daxpay.multi.core.exception;

import cn.daxpay.multi.core.code.DaxPayErrorCode;

/**
 * 交易失败
 * @author xxm
 * @since 2024/6/17
 */
public class TradeFailException extends PayFailureException{

    public TradeFailException(String message) {
        super(DaxPayErrorCode.TRADE_FAIL,message);
    }

    public TradeFailException() {
        super(DaxPayErrorCode.TRADE_FAIL,"交易失败");
    }
}
