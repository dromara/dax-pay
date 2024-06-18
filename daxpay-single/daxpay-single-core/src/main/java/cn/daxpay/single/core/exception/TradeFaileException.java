package cn.daxpay.single.core.exception;

import cn.daxpay.single.core.code.DaxPayCommonErrorCode;

/**
 * 交易失败
 * @author xxm
 * @since 2024/6/17
 */
public class TradeFaileException extends PayFailureException{

    public TradeFaileException(String message) {
        super(DaxPayCommonErrorCode.TRADE_FAILE,message);
    }

    public TradeFaileException() {
        super(DaxPayCommonErrorCode.TRADE_FAILE,"交易失败");
    }
}
