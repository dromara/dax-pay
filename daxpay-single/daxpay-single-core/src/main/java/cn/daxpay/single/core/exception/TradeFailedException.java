package cn.daxpay.single.core.exception;

import cn.daxpay.single.core.code.DaxPayCommonErrorCode;

/**
 * 交易失败
 * @author xxm
 * @since 2024/6/17
 */
public class TradeFailedException extends PayFailureException{

    public TradeFailedException(String message) {
        super(DaxPayCommonErrorCode.TRADE_FAILED,message);
    }

    public TradeFailedException() {
        super(DaxPayCommonErrorCode.TRADE_FAILED,"交易失败");
    }
}
